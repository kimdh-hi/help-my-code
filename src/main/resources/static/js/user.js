// 로그인
function signin() {
    console.log('로그인')
    let username = $('#signin-id').val()
    let password = $('#signin-password').val()

    let data = {
        "username": username,
        "password": password
    }

    $.ajax({
        type: "POST",
        url: "/user/signin",
        contentType: "application/json;charset=utf-8;",
        data: JSON.stringify(data),
        success: function(res) {
            sessionStorage.setItem("mytoken", res['token'])
            sessionStorage.setItem("myAuthority", res['authority'])
            alert('로그인에 성공했습니다.')
            window.location.reload()
        }, error: function(err) {
            alert('로그인에 실패했습니다.')
        }
    })
}

// 회원가입
function signup() {
    let id = $('#signup-id').val()
    let password = $('#signup-password').val()
    let isReviewer = $('input[id="signup-isReviewer"]').is(":checked")
    let data = {}
    let langs = []
    if (isReviewer) {
        $('input:checkbox[name="languageSelectBox"]').each(function() {
            if (this.checked) {
                langs.push($(this).val())
            }
        });
    }

    data = {
        "username": id,
        "password": password,
        "reviewer": isReviewer,
        "languages": langs
    }


    $.ajax({
        type: "POST",
        url: "/user/signup",
        contentType: "application/json;charset=utf-8;",
        data: JSON.stringify(data),
        success: function (res) {
            alert('회원가입에 성공했습니다.')
            window.location.reload()
        }, error: function(err) {
            alert('회원가입에 실패했습니다.')
        }
    })
}

function logout() {
    $.ajax({
        type: "POST",
        url: "/user/logout",
        success: function(res) {
            if (res['result'] == "success") {
                sessionStorage.clear();
                alert(res['message'])
            }
        }
    })
    window.location.reload();
}

function openSigninModal() {
    $('#signin-id').val('')
    $('#signin-password').val('')
    $('#signinModal').modal('show');
}

function openSignupModal() {
    $('#signup-id').val('')
    $('#signup-password').val('')
    $('#signupModal').modal('show');
}