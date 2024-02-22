$(document).ready(function () {
    function checkPasswordMatch() {
        var password = $("#password").val();
        var repeatPassword = $("#repeatPassword").val();
        var passwordError = $("#password-error");

        if (password !== repeatPassword) {
            passwordError.text("Passwords do not match");
            return false;
        } else {
            passwordError.text("");
            return true;
        }
    }

    $("#password, #repeatPassword").on("input", function () {
        checkPasswordMatch();
    });

    $("form").submit(function (event) {
        if (!checkPasswordMatch()) {
            event.preventDefault();
        }
    });
});
