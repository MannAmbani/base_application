package com.rnd.baseproject.tools

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.CountDownTimer
import android.view.View
import androidx.core.util.Pair
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener


object DialogUtils {


    lateinit var progressDialog: Dialog
    lateinit var internetDialog: Dialog
    lateinit var dialog: Dialog
    var countDownTimer: CountDownTimer? = null

    fun loadingDialog(activity: Context?, message: String) {
//        dialog = Dialog(activity!!, R.style.DialogStyle)
//        val dialogbinding: DialogLoadingBinding =
//            DialogLoadingBinding.inflate(LayoutInflater.from(activity))
//        dialog.setContentView(dialogbinding.root)
//
//        dialog.setCancelable(false)
//        //val  ivMessage = dialog.findViewById<TextView>(R.id.ivMessage)
//        if (message.isNotEmpty()) {
//            dialogbinding.ivMessage.text = Html.fromHtml(message)
//        }
//        try {
//            if (this::dialog.isInitialized) {
//                if (!dialog.isShowing) {
//                    dialog.show()
//                }
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
    }

    fun loadingDialog(activity: Context?) {

//        dialog = Dialog(activity!!, R.style.DialogStyle)
//        dialog.setContentView(R.layout.dialog_loading)
//        dialog.setCancelable(false)
//
//        try {
//            if (this::dialog.isInitialized) {
//                if (!dialog.isShowing) {
//                    dialog.show()
//                }
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
    }

    @SuppressLint("InflateParams")
    fun showAlertDialog(
        mContext: Context?,
        title: String?,
        message: String?,
        buttonPositive: String?,
        buttonNegative: String?,
        onClickListener: View.OnClickListener
    ) {
//        dialog = Dialog(mContext!!, R.style.DialogStyle)
//        val adBinding: DialogAlertBinding =
//            DialogAlertBinding.bind(
//                LayoutInflater.from(mContext).inflate(R.layout.dialog_alert, null)
//            )
//        dialog.setContentView(adBinding.root)
//        dialog.setCanceledOnTouchOutside(true)
//        adBinding.tvTitle.text = title
//        adBinding.tvMessage.text = message
//        val btnCancel: Button = dialog.findViewById(R.id.btnCancel)
//        val btnOk: Button = dialog.findViewById(R.id.btnOk)
//        btnCancel.text = buttonNegative
//        btnOk.text = buttonPositive
//        btnOk.setOnClickListener {
//            dialog.cancel()
//            onClickListener.onClick(adBinding.layBtn.btnOk)
//        }
//        btnCancel.setOnClickListener {
//            dialog.cancel()
//        }
//        show()

    }

    fun showAlertDialog(
        mContext: Context?,
        title: String?,
        message: String?,
        buttonNeutral: String?,
        onClickListener: View.OnClickListener
    ) {

//        dialog = Dialog(mContext!!, R.style.DialogStyle)
//        val adBinding: DialogAlertBinding =
//            DialogAlertBinding.bind(
//                LayoutInflater.from(mContext).inflate(R.layout.dialog_alert, null)
//            )
//        dialog.setContentView(adBinding.root)
//        dialog.setCanceledOnTouchOutside(true)
//
//        adBinding.tvTitle.text = title
//        adBinding.tvMessage.text = message
//        val btnCancel: Button = dialog.findViewById(R.id.btnCancel)
//        val btnOk: Button = dialog.findViewById(R.id.btnOk)
//        btnCancel.text = buttonNeutral
//        btnOk.visibility = View.GONE
//
//        btnOk.setOnClickListener {
//            //onClickListener.onClick(dialog.btnSubmit!!)
//        }
//        btnCancel.setOnClickListener {
//            onClickListener.onClick(btnCancel)
//        }
//        show()
    }


    private fun show() {
        try {
            if (this::dialog.isInitialized) {
                if (!dialog.isShowing) {
                    dialog.show()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun cancel() {
        try {
            if (this::dialog.isInitialized) {
                if (dialog.isShowing) dialog.cancel()
                if (countDownTimer != null) countDownTimer!!.cancel()
            }
            if (this::progressDialog.isInitialized) {
                progressDialog.cancel()
                Thread.interrupted()
            }
            if (this::internetDialog.isInitialized) internetDialog.cancel()
        } catch (iil: IllegalAccessException) {
            iil.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showSelectionDialog(
        mContext: Context,
        title: String?,
        array: Array<String>,
        onClickListener: DialogInterface.OnClickListener
    ) {
//        val builder = AlertDialog.Builder(mContext, R.style.SelectionDialogStyles)
//        builder.setTitle(title)
//            .setItems(
//                array
//            ) { dialog, which -> onClickListener.onClick(dialog, which) }
//            .setPositiveButton(
//                mContext.getString(R.string.cancel)
//            ) { dialog, _ -> dialog.dismiss() }
//        builder.create().show()
    }

    fun showUserAccessDialog(
        mContext: Context, result: (Boolean) -> Unit
    ) {
//        val builder = AlertDialog.Builder(mContext, R.style.SelectionDialogStyles)
//        builder.setTitle(mContext.getString(R.string.you_are_admin_way_by_you_are))
//            .setItems(
//                arrayOf(
//                    mContext.getString(R.string.service_user),
//                    mContext.getString(R.string.sales_user)
//                )
//            ) { _, which ->
//                if (which == 0) {
//                    result.invoke(UserAccess.SERVICE_USER)
//                } else {
//                    result.invoke(UserAccess.SALES_USER)
//                }
//            }
//            .setPositiveButton(
//                mContext.getString(R.string.cancel)
//            ) { dialog, _ -> dialog.dismiss() }
//        builder.create().show()
    }

    fun showSingleChoiceDialog(
        mContext: Context,
        title: String?,
        array: Array<String>,
        selection: Int,
        onSelectionClickListener: DialogInterface.OnClickListener,
        onClickListener: DialogInterface.OnClickListener
    ) {
//        val builder = AlertDialog.Builder(mContext, R.style.SelectionDialogStyles)
//        builder.setTitle(title)
//            .setSingleChoiceItems(
//                array, selection
//            ) { dialog, which -> onSelectionClickListener.onClick(dialog, which) }
//            .setPositiveButton(
//                mContext.getString(R.string.ok)
//            ) { dialog, which -> onClickListener.onClick(dialog, which) }
//            .setNegativeButton(
//                mContext.getString(R.string.cancel)
//            ) { dialog, _ -> dialog.dismiss() }
//        builder.create().show()
    }

    fun showDatePickerDialog(
        fragmentManager: androidx.fragment.app.FragmentManager,
        onPositiveButtonClickListener: MaterialPickerOnPositiveButtonClickListener<Long>
    ) {
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

        datePicker.show(fragmentManager, "DatePicker")
        datePicker.addOnPositiveButtonClickListener(onPositiveButtonClickListener)
    }

    fun showDateRangePickerDialog(
        fragmentManager: FragmentManager,
        fromDate: Long,
        toDate: Long,
        onPositiveButtonClickListener: MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>
    ) {

        val datePicker =
            MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Select date range")
                .setSelection(Pair(fromDate, toDate))
                .build()
        datePicker.show(fragmentManager, "DatePicker")
        datePicker.addOnPositiveButtonClickListener(onPositiveButtonClickListener)
    }


}