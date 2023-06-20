package com.rnd.baseproject.activity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.DialogFragment
import com.rnd.baseproject.R
import com.rnd.baseproject.tools.ShowSelectionDialog
import com.rnd.baseproject.ui.theme.BaseProjectTheme


class PicModeSelectDialogFragment : DialogFragment() {
    private val picMode = arrayOf(PicModes.CAMERA, PicModes.GALLERY)
    private var iPicModeSelectListener: IPicModeSelectListener? = null
    private var position = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                BaseProjectTheme {
                    ShowSelectionDialog(neutralButtonText = "Cancel",
                        title = getString(R.string.select_image),
                        arrayList = arrayListOf(picMode[0], picMode[1]),
                        onNeutralButtonClick = {
                            dismiss()
                        },
                        onItemClick = { index: Int, s: String ->
                            if (iPicModeSelectListener != null) {
                                dismiss()
                                iPicModeSelectListener!!.onPicModeSelected(
                                    picMode[index],
                                    position
                                )
                                Log.e("TAG", "NOT NULL")
                            } else {
                                Log.e("TAG", " NULL")
                            }
                        })
                }
            }
        }
    }

//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        val builder =
//            AlertDialog.Builder(activity)
//        builder.setTitle(getString(R.string.select_image))
//        builder.setItems(picMode) { _, which ->
//            if (iPicModeSelectListener != null) {
//                iPicModeSelectListener!!.onPicModeSelected(picMode[which], position)
//                Log.e("TAG", "NOT NULL")
//            } else {
//                Log.e("TAG", " NULL")
//            }
//        }
//        builder.setNegativeButton(
//            "Cancel"
//        ) { _, _ -> }
//        return builder.create()
//    }

    fun setiPicModeSelectListener(
        iPicModeSelectListener: IPicModeSelectListener?,
        pos: Int
    ) {
        position = pos
        this.iPicModeSelectListener = iPicModeSelectListener
    }

    interface IPicModeSelectListener {
        fun onPicModeSelected(mode: String?, position: Int)
    }

    interface PicModes {
        companion object {
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