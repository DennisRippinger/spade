var statusDlgTimer = null;

function showStatusDialog() {
    if (statusDlgTimer === null) {
        statusDlgTimer = setTimeout("statusDialog.show()", 100);
    }
}

function hideStatusDialog() {
    if (statusDlgTimer !== null) {
        clearTimeout(statusDlgTimer);
        if (statusDialog != null) {
            statusDialog.hide();
        }
        statusDlgTimer = null;
    }
}