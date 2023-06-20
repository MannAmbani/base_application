package com.rnd.baseproject.activity


import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ContentResolver
import android.content.Intent
import android.graphics.*
import android.graphics.Bitmap.CompressFormat
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.content.FileProvider
import com.albinmathew.photocrop.cropoverlay.CropOverlayView
import com.albinmathew.photocrop.cropoverlay.edge.Edge
import com.albinmathew.photocrop.cropoverlay.utils.ImageViewUtil
import com.albinmathew.photocrop.photoview.PhotoView
import com.rnd.baseproject.tools.InternalStorageContentProvider
import com.rnd.baseproject.tools.getImageUri
import com.rnd.baseproject.R
import java.io.*

class ImageCropActivity : Activity() {
    private val IMAGE_MAX_SIZE = 1024
    private val mOutputFormat = CompressFormat.JPEG
    private var mImageView: PhotoView? = null
    private var mCropOverlayView: CropOverlayView? = null
    private var btnCancel: Button? = null
    private var btnSend: Button? = null
    private var mContentResolver: ContentResolver? = null
    private var minScale = 1f

    //Temp file to save cropped image
    private var mImagePath: String? = null
    private var mSaveUri: Uri? = null
    private var mImageUri: Uri? = null

    //File for capturing camera images
    private var mFileTemp: File? = null
    private val btnCancelListener =
        View.OnClickListener { userCancelled() }
    private val btnSendListener =
        View.OnClickListener { saveUploadCroppedImage() }
    private var tag: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_crop)
        mContentResolver = contentResolver
        mImageView = findViewById<View>(R.id.iv_photo) as PhotoView
        mCropOverlayView = findViewById<View>(R.id.crop_overlay) as CropOverlayView
        btnSend = findViewById<View>(R.id.sendBtn) as Button
        btnSend!!.setOnClickListener(btnSendListener)
        btnCancel = findViewById<View>(R.id.cancelBtn) as Button
        btnCancel!!.setOnClickListener(btnCancelListener)
        mImageView!!.addListener {
            Rect(
                Edge.LEFT.coordinate.toInt(),
                Edge.TOP.coordinate.toInt(),
                Edge.RIGHT.coordinate.toInt(),
                Edge.BOTTOM.coordinate.toInt()
            )
        }
        createTempFile()
        if (savedInstanceState == null || !savedInstanceState.getBoolean("restoreState")) {
            val action = intent.getStringExtra("ACTION")
            tag = intent.getStringExtra("tag")
            if (null != action) {
                when (action) {
                    PicModeSelectDialogFragment.IntentExtras.ACTION_CAMERA -> {
                        intent.removeExtra("ACTION")
                        takePic()
                        return
                    }
                    PicModeSelectDialogFragment.IntentExtras.ACTION_GALLERY -> {
                        intent.removeExtra("ACTION")
                        pickImage()
                        return
                    }
                }
            }
        }
        mImagePath = mFileTemp!!.path
        mSaveUri = getImageUri(mImagePath)
        mImageUri = getImageUri(mImagePath)
        init()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
    }

    private fun init() {
        val b = getBitmap(mImageUri)
        val bitmap: Drawable = BitmapDrawable(resources, b)
        val h = bitmap.intrinsicHeight
        val w = bitmap.intrinsicWidth
        val cropWindowWidth: Float = Edge.getWidth()
        val cropWindowHeight: Float = Edge.getHeight()
        if (h <= w) {
            minScale = (cropWindowHeight + 1f) / h
        } else if (w < h) {
            minScale = (cropWindowWidth + 1f) / w
        }
        mImageView!!.maximumScale = minScale * 3
        mImageView!!.mediumScale = minScale * 2
        mImageView!!.minimumScale = minScale
        mImageView!!.setImageDrawable(bitmap)
        mImageView!!.scale = minScale
    }

    private fun saveUploadCroppedImage() {
        val saved = saveOutput()
        if (saved) { //USUALLY Upload image to server here
            val intent = Intent()
            intent.putExtra(PicModeSelectDialogFragment.IntentExtras.IMAGE_PATH, mImagePath)
            setResult(RESULT_OK, intent)
            finish()
        } else {
            Toast.makeText(
                this,
                getString(R.string.unable_to_save_image_into_your_device),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun createTempFile() {
        val state = Environment.getExternalStorageState()
        mFileTemp = if (Environment.MEDIA_MOUNTED == state) {
            val f =
                File(getExternalFilesDir(null)!!.absolutePath.toString() + "/.oob")
            if (!f.exists()) {
                f.mkdir()
            }
            File(
                getExternalFilesDir(null)!!.absolutePath.toString() + "/.oob",
                System.currentTimeMillis().toString() + TEMP_PHOTO_FILE_NAME
            )
        } else {
            File(
                filesDir,
                System.currentTimeMillis().toString() + TEMP_PHOTO_FILE_NAME
            )
        }
    }

    private fun takePic() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            var mImageCaptureUri: Uri? = null
            val state = Environment.getExternalStorageState()
            mImageCaptureUri =
                if (Environment.MEDIA_MOUNTED == state) { //                mImageCaptureUri = Uri.fromFile(mFileTemp);
                    FileProvider.getUriForFile(
                        this,
                        applicationContext
                            .packageName + ".provider", mFileTemp!!
                    )
                    //                install.setDataAndType(apkURI, mimeType);
                    //                install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                } else { /*
                         * The solution is taken from here: http://stackoverflow.com/questions/10042695/how-to-get-camera-result-as-a-uri-in-data-folder
                         */
                    InternalStorageContentProvider.CONTENT_URI
                }
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri)
            takePictureIntent.putExtra("return-data", true)
            startActivityForResult(
                takePictureIntent,
                REQUEST_CODE_TAKE_PICTURE
            )
        } catch (e: ActivityNotFoundException) {
            Log.e(TAG, getString(R.string.cant_take_picture), e)
            Toast.makeText(this, getString(R.string.cant_take_picture), Toast.LENGTH_LONG).show()
        }
    }

    public override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putBoolean("restoreState", true)
    }

    private fun pickImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).setType("image/*")
        try {
            startActivityForResult(intent, REQUEST_CODE_PICK_GALLERY)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, getString(R.string.no_image_source_available), Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        result: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, result)
        //        createTempFile();
        if (requestCode == REQUEST_CODE_TAKE_PICTURE) {
            if (resultCode == RESULT_OK) {
                mImagePath = mFileTemp!!.path
                mSaveUri = getImageUri(mImagePath)
                mImageUri = getImageUri(mImagePath)
                if (tag.equals(
                        "curtain",
                        ignoreCase = true
                    )
                ) { //USUALLY Upload image to server here
                    val intent = Intent()
                    intent.putExtra(PicModeSelectDialogFragment.IntentExtras.IMAGE_PATH, mImagePath)
                    setResult(RESULT_OK, intent)
                    finish()
                } else {
                    init()
                }
            } else if (resultCode == RESULT_CANCELED) {
                userCancelled()
            } else {
                errored()
            }
        } else if (requestCode == REQUEST_CODE_PICK_GALLERY) {
            if (resultCode == RESULT_CANCELED) {
                userCancelled()
                return
            } else if (resultCode == RESULT_OK) {
                try {
                    val inputStream =
                        contentResolver.openInputStream(result!!.data!!) // Got the bitmap .. Copy it to the temp file for cropping
                    val fileOutputStream =
                        FileOutputStream(mFileTemp)
                    copyStream(inputStream, fileOutputStream)
                    fileOutputStream.close()
                    inputStream!!.close()
                    mImagePath = mFileTemp!!.path
                    mSaveUri = getImageUri(mImagePath)
                    mImageUri = getImageUri(mImagePath)
                    if (tag.equals(
                            "curtain",
                            ignoreCase = true
                        )
                    ) { //USUALLY Upload image to server here
                        val intent = Intent()
                        intent.putExtra(
                            PicModeSelectDialogFragment.IntentExtras.IMAGE_PATH,
                            mImagePath
                        )
                        setResult(RESULT_OK, intent)
                        finish()
                    } else {
                        init()
                    }
                } catch (e: Exception) {
                    errored()
                }
            } else {
                errored()
            }
        }
    }

    private fun getBitmap(uri: Uri?): Bitmap? {
        var `in`: InputStream? = null
        var returnedBitmap: Bitmap? = null
        try {
            `in` = mContentResolver!!.openInputStream(uri!!)
            //Decode image size
            val o = BitmapFactory.Options()
            o.inJustDecodeBounds = true
            BitmapFactory.decodeStream(`in`, null, o)
            `in`!!.close()
            var scale = 1
            if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
                scale = Math.pow(
                    2.0,
                    Math.round(
                        Math.log(
                            IMAGE_MAX_SIZE / Math.max(
                                o.outHeight,
                                o.outWidth
                            ).toDouble()
                        ) / Math.log(0.5)
                    ).toDouble()
                ).toInt()
            }
            val o2 = BitmapFactory.Options()
            o2.inSampleSize = scale
            `in` = mContentResolver!!.openInputStream(uri)
            var bitmap = BitmapFactory.decodeStream(`in`, null, o2)
            `in`!!.close()
            //First check
            val ei = ExifInterface(uri.path.toString())
            val orientation =
                ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> {
                    returnedBitmap = rotateImage(bitmap, 90f)
                    //Free up the memory
                    bitmap!!.recycle()
                    bitmap = null
                }
                ExifInterface.ORIENTATION_ROTATE_180 -> {
                    returnedBitmap = rotateImage(bitmap, 180f)
                    //Free up the memory
                    bitmap!!.recycle()
                    bitmap = null
                }
                ExifInterface.ORIENTATION_ROTATE_270 -> {
                    returnedBitmap = rotateImage(bitmap, 270f)
                    //Free up the memory
                    bitmap!!.recycle()
                    bitmap = null
                }
                else -> returnedBitmap = bitmap
            }
            return returnedBitmap
        } catch (e: FileNotFoundException) {
            Log.d(TAG, "FileNotFoundException")
        } catch (e: IOException) {
            Log.d(TAG, "IOException")
        }
        return null
    }

    private val currentDisplayedImage: Bitmap
        private get() {
            val result = Bitmap.createBitmap(
                mImageView!!.getWidth(),
                mImageView!!.getHeight(),
                Bitmap.Config.RGB_565
            )
            val c = Canvas(result)
            mImageView!!.draw(c)
            return result
        }

    // Get the scale factor between the actual Bitmap dimensions and the
// displayed dimensions for width.
    private val croppedImage: Bitmap
        get() {
            val mCurrentDisplayedBitmap = currentDisplayedImage
            val displayedImageRect: Rect =
                ImageViewUtil.getBitmapRectCenterInside(mCurrentDisplayedBitmap, mImageView)
            // Get the scale factor between the actual Bitmap dimensions and the
            // displayed dimensions for width.
            val actualImageWidth = mCurrentDisplayedBitmap.width.toFloat()
            val displayedImageWidth = displayedImageRect.width().toFloat()
            val scaleFactorWidth = actualImageWidth / displayedImageWidth
            // Get the scale factor between the actual Bitmap dimensions and the
            // displayed dimensions for height.
            val actualImageHeight = mCurrentDisplayedBitmap.height.toFloat()
            val displayedImageHeight = displayedImageRect.height().toFloat()
            val scaleFactorHeight = actualImageHeight / displayedImageHeight
            // Get crop window position relative to the displayed image.
            val cropWindowX: Float = Edge.LEFT.getCoordinate() - displayedImageRect.left
            val cropWindowY: Float = Edge.TOP.getCoordinate() - displayedImageRect.top
            val cropWindowWidth: Float = Edge.getWidth()
            val cropWindowHeight: Float = Edge.getHeight()
            // Scale the crop window position to the actual size of the Bitmap.
            val actualCropX = cropWindowX * scaleFactorWidth
            val actualCropY = cropWindowY * scaleFactorHeight
            val actualCropWidth = cropWindowWidth * scaleFactorWidth
            val actualCropHeight = cropWindowHeight * scaleFactorHeight
            // Crop the subset from the original Bitmap.
            return Bitmap.createBitmap(
                mCurrentDisplayedBitmap,
                actualCropX.toInt(),
                actualCropY.toInt(),
                actualCropWidth.toInt(),
                actualCropHeight.toInt()
            )
        }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            androidx.appcompat.R.id.home -> {
                super.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveOutput(): Boolean {
        val croppedImage = croppedImage
        if (mSaveUri != null) {
            var outputStream: OutputStream? = null
            try {
                outputStream = mContentResolver!!.openOutputStream(mSaveUri!!)
                if (outputStream != null) {
                    croppedImage.compress(mOutputFormat, 90, outputStream)
                }
            } catch (ex: IOException) {
                ex.printStackTrace()
                return false
            } finally {
                closeSilently(outputStream)
            }
        } else {
            Log.e(TAG, "not defined image url")
            return false
        }
        croppedImage.recycle()
        return true
    }

    fun closeSilently(c: Closeable?) {
        if (c == null) return
        try {
            c.close()
        } catch (t: Throwable) { // do nothing
        }
    }

    private fun rotateImage(source: Bitmap?, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            source!!,
            0,
            0,
            source.width,
            source.height,
            matrix,
            true
        )
    }

    fun userCancelled() {
        val intent = Intent()
        setResult(RESULT_CANCELED, intent)
        finish()
    }

    fun errored() {
        val intent = Intent()
        intent.putExtra(ERROR, true)
        if ("Error while opening the image file. Please try again." != null) {
            intent.putExtra(
                ERROR_MSG,
                "Error while opening the image file. Please try again."
            )
        }
        finish()
    }

    companion object {
        const val TAG = "ImageCropActivity"
        const val TEMP_PHOTO_FILE_NAME = "temp_photo.jpg"
        const val REQUEST_CODE_PICK_GALLERY = 0x1
        const val REQUEST_CODE_TAKE_PICTURE = 0x2
        const val REQUEST_CODE_CROPPED_PICTURE = 0x3
        const val ERROR_MSG = "error_msg"
        const val ERROR = "error"
        private const val REQUEST_WRITE_STORAGE = 112
        const val REQUEST_CODE_UPDATE_PIC = 0x1

        @Throws(IOException::class)
        fun copyStream(
            input: InputStream?,
            output: OutputStream
        ) {
            val buffer = ByteArray(1024)
            var bytesRead: Int
            while (input!!.read(buffer).also { bytesRead = it } != -1) {
                output.write(buffer, 0, bytesRead)
            }
        }
    }
}