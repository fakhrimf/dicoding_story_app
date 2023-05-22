package com.fakhrimf.storyapp.ui.addstory

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.fakhrimf.storyapp.databinding.FragmentAddStoryBinding
import com.fakhrimf.storyapp.model.LoginResult
import com.fakhrimf.storyapp.model.PostResponse
import com.fakhrimf.storyapp.utils.ApiClient
import com.fakhrimf.storyapp.utils.ApiInterface
import com.fakhrimf.storyapp.utils.CAMERA_REQUEST_CODE
import com.fakhrimf.storyapp.utils.IMAGE_REQUEST_CODE
import com.fakhrimf.storyapp.utils.IMAGE_TYPE
import com.fakhrimf.storyapp.utils.MAXIMAL_SIZE
import com.fakhrimf.storyapp.utils.boilerplate.MyFragment
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.util.Locale

class AddStoryFragment : MyFragment() {
    private var _binding: FragmentAddStoryBinding? = null
    private val binding get() = _binding!!
    private var path: Uri? = null
    private var photoPath: String? = null
    private var file: File? = null
    private val apiInterface: ApiInterface by lazy {
        ApiClient.getClient().create(ApiInterface::class.java)
    }
    private val timeStamp: String = SimpleDateFormat(
        "hh:mm",
        Locale.US
    ).format(System.currentTimeMillis())

    companion object {
        fun newInstance() = AddStoryFragment()
    }

    private fun getImageIntent(): Intent {
        val intent = Intent()
        intent.apply {
            type = IMAGE_TYPE
            action = Intent.ACTION_GET_CONTENT
        }
        return Intent.createChooser(intent, "Pilih gambar yang ingin digunakan")
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(requireActivity().packageManager)
        createCustomTempFile(requireActivity().application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                requireActivity(),
                "com.fakhrimf.storyapp.provider",
                it
            )
            photoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            startActivityForResult(intent, CAMERA_REQUEST_CODE)
        }
    }

    private lateinit var viewModel: AddStoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[AddStoryViewModel::class.java]
    }

    private fun init() {
        binding.cvGallery.setOnClickListener {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_CODE
            )
            startActivityForResult(getImageIntent(), IMAGE_REQUEST_CODE)
        }
        binding.cvCamera.setOnClickListener {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_CODE
            )
            startTakePhoto()
        }
        binding.btnSubmit.setOnClickListener {
            upload()
        }
    }

    private fun getImageBitmap(contentResolver: ContentResolver, path: Uri): Bitmap {
        @Suppress("DEPRECATION") return ImageDecoder.decodeBitmap(
            ImageDecoder.createSource(
                contentResolver,
                path
            )
        )
    }

    private fun createCustomTempFile(context: Context): File {
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(timeStamp, ".jpg", storageDir)
    }

    private fun uriToFile(selectedImg: Uri, context: Context): File {
        val contentResolver: ContentResolver = context.contentResolver
        val myFile = createCustomTempFile(context)

        val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
        val outputStream: OutputStream = FileOutputStream(myFile)
        val buf = ByteArray(1024)
        var len: Int
        while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
        outputStream.close()
        inputStream.close()
        return myFile
    }

    private fun rotateFile(file: File, isBackCamera: Boolean = false): File {
        val matrix = Matrix()
        val bitmap = BitmapFactory.decodeFile(file.path)
        val rotation = if (isBackCamera) 90f else -90f
        matrix.postRotate(rotation)
        if (!isBackCamera) {
            matrix.postScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f)
        }
        val result = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        result.compress(Bitmap.CompressFormat.JPEG, 100, FileOutputStream(file))
        return file
    }

    private fun reduceFileImage(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)
        var compressQuality = 100
        var streamLength: Int
        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > MAXIMAL_SIZE)
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
        return file
    }

    private fun upload() {
        binding.llShadow.visibility = View.VISIBLE
        binding.spinKit.visibility = View.VISIBLE
        if (file != null) {
            val desc = binding.etStory.text.toString()
            if (desc.isNotEmpty()) {
                file = reduceFileImage(file!!)
                val descPart = desc.toRequestBody()
                val req = file!!.asRequestBody("image/jpeg".toMediaType())
                val image = MultipartBody.Part.createFormData(
                    "photo", file!!.name, req
                )
                val loginResult = Gson().fromJson(getLoginKey()!!, LoginResult::class.java)
                val call = apiInterface.addStory(
                    "Bearer " + loginResult.token, descPart, image
                )
                call.enqueue(object : Callback<PostResponse> {
                    override fun onResponse(
                        call: Call<PostResponse>,
                        response: Response<PostResponse>
                    ) {
                        requireActivity().finish()
                        binding.llShadow.visibility = View.GONE
                        binding.spinKit.visibility = View.GONE
                        Log.d("SUCCESS", "onResponse: $response")
                        toastySuccess(response.message())
                    }

                    override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                        call.cancel()
//                        requireActivity().finish()
                        binding.llShadow.visibility = View.GONE
                        binding.spinKit.visibility = View.GONE+
                        Log.d("ERROR", "onResponse: ${t.message.toString()}")
                        toastyError(t.message.toString())
                    }

                })
            } else {
                toastyError("ISI STORY DULU, GANTI INI JADI STRING RES")
            }
        } else {
            toastyError("UPLOAD GAMBAR DULU, GANTI INI JADI STRING RES")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("cape aing", "onActivityResult: $data")
        if (requestCode == IMAGE_REQUEST_CODE && data != null && data.data != null) {
            path = data.data!!
            file = uriToFile(path!!, requireContext())
            file!!.mkdirs()
            val bitmap = getImageBitmap(requireActivity().contentResolver, path!!)
            binding.ivHeader.setImageBitmap(bitmap)
        } else if (requestCode == CAMERA_REQUEST_CODE) {
            file = File(photoPath!!)
//            file = rotateFile(file!!, true)
            val result = BitmapFactory.decodeFile(file!!.path)
            binding.ivHeader.setImageBitmap(result)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }
}