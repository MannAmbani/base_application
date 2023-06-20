package com.rnd.baseproject.tools

val REQUEST_CODE = 200
val IMAGE_PICK_CODE = 111
val PICK_PDF_FILE = 2

object ConstValue {

    const val REQUEST_CODE_UPDATE_PIC = 0x1
    const val REQUEST_CODE_SIGNATURE = 0x2
    const val TEMP_PHOTO_FILE_NAME = "temp_photo.jpg"
    const val MULTIPLE_PERMISSION_REQUEST_CODE = 111
    const val REQUEST_CODE_PERMISSION_SETTING = 103
    const val REQUEST_CODE_EXPENSE_FORM = 102
    const val APPLY_LEAVE_REQUEST_CODE = 104

    const val APP_DATABASE_NAME = "associative_db"

    interface PicModes {
        companion object {
            //        String CAMERA = "Camera";
            //        String GALLERY = "Gallery";
            const val CAMERA = "Take Photo"
            const val GALLERY = "Choose from Gallery"
            const val IS_MULTI_SELECT = "isMultiSelect"
        }
    }

    interface IntentExtras {
        companion object {
            const val ACTION_CAMERA = "action-camera"
            const val ACTION_GALLERY = "action-gallery"
            const val IMAGE_PATH = "image-path"
        }
    }
}