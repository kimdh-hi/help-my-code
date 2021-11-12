
$(document).ready(function() {

    loginCheck()

    $("#signup-isReviewer").change(function() {
        if( $("#signup-isReviewer").is(":checked")) {
            $('#language-check-box').show()
        } else {
            $('#language-check-box').hide()
        }
    })

    $("#codeReviewRequestLanguageSelectBox").on("change", function() {
        let language = $(this).val().toString()

        $.ajax({
            type: "GET",
            url: `/review/language/user?language=${language}`,
            success: function(res) {
                $('#codeReviewRequestReviewerSelectBox').empty()
                let tmp_html = "<option selected>리뷰어 선택</option>"
                $('#codeReviewRequestReviewerSelectBox').append(tmp_html)
                for (let i=0; i<res.length; i++) {
                    let tmp_html = `<option value="${res[i]['userId']}">${res[i]['username']}</option>`
                    $('#codeReviewRequestReviewerSelectBox').append(tmp_html)
                }
                $('#reviewerSelectDiv').show();
            }
        })
    })

    $("#codeReviewRequestReviewerSelectBox").on("change", function() {
        let userId = $(this).val()
        console.log(userId)
        $('#requestCodeReviewBtn').show()

        let data = {

        }

        $.ajax({
            type: "POST",
            url: "/reivew",
            contentType: "application/json;charset=utf-8;",
            data: data,
            success: function(res) {
                console.log(res)
            }
        })
    })
})

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
            console.log(res)
            sessionStorage.setItem("mytoken", res['token'])
            alert('로그인에 성공했습니다.')
            window.location.reload()
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
        "isReviewer": isReviewer,
        "languages": langs
    }

    $.ajax({
        type: "POST",
        url: "/user/signup",
        contentType: "application/json;charset=utf-8;",
        data: JSON.stringify(data),
        success: function (res) {
            console.log(res)
            alert('회원가입에 성공했습니다.')
            window.location.reload()
        }
    })
}

function logout() {
    sessionStorage.clear();
    window.location.reload();
}

function loginCheck() {
    // 인증이 된 경우
    if (sessionStorage.getItem("mytoken") != null) {
        $('#signinBtn').hide()
        $('#signupBtn').hide()
        $('#logoutBtn').show()

        $('#reviewRequestBtn').show()
        $('#reviewAnswerBtn').show()
    } else { // 인증이 되지 않은 경우
        $('#signinBtn').show()
        $('#signupBtn').show()
        $('#logoutBtn').hide()

        $('#reviewRequestBtn').hide()
        $('#reviewAnswerBtn').hide()
    }
}

function showCodeReviewRequestForm() {
    console.log("코드리뷰요청 폼 열기")
    let display = $('#codeReviewRequestForm').css("display")

    if (display == "none") {
        $('#codeReviewRequestForm').css({"display": "block"})
    } else {
        $('#codeReviewRequestForm').css({"display": "none"})
    }
}

function requestCodeReview() {
    let title = $('#codeReviewTitle').val()
    let code = $('#codeReviewCode').val()
    let comment = $('#codeReviewComment').val()

    $.ajax({
        type: "POST",
        url: "/review",
        contentType: "application/json;charset=utf-8",

    })
}

function openSigninModal() {
    $('#signinModal').modal('show');
}

function openSignupModal() {
    $('#signupModal').modal('show');
}

// ajax 요청시 token이 있다면 헤더에 추가하도록 설정
$.ajaxPrefilter(function( options, originalOptions, jqXHR ) {
    if (sessionStorage.getItem('mytoken') != null) {
        jqXHR.setRequestHeader('Authorization', 'Bearer ' + sessionStorage.getItem('mytoken'));
    }
});