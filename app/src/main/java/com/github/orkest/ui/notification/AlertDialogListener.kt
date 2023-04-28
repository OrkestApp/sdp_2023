package com.github.orkest.ui.notification

interface AlertDialogListener {
    /**
     * Method to listen to the alert dialog shown
     */
    fun onAlertDialogShown(alertDialog: androidx.appcompat.app.AlertDialog)
}