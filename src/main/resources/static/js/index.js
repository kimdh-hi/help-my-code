
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

    })
})

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
        $('#codeReviewRequestLanguageSelectBox').empty()
        let tmp_html = `<option selected>언어 선택</option>
                <option value="Java">Java</option>
                <option value="Python">Python</option>
                <option value="Javascript">Javascript</option>
                <option value="C++">C++</option>
                <option value="HTML">HTML</option>
                <option value="CSS">C++</option>`
        $('#codeReviewRequestLanguageSelectBox').append(tmp_html)
    } else {
        $('#codeReviewRequestForm').css({"display": "none"})
    }
}

function requestCodeReview() {
    let title = $('#codeReviewTitle').val()
    let code = $('#codeReviewCode').val()
    let comment = $('#codeReviewComment').val()
    let language = $('#codeReviewRequestLanguageSelectBox').val()
    let reviewerId = $('#codeReviewRequestReviewerSelectBox').val()

    data = {
        "reviewerId": reviewerId,
        "title": title,
        "code": code,
        "comment": comment,
        "language": language
    }

    $.ajax({
        type: "POST",
        url: "/review",
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify(data),
        success: function(res){
            if (res['result'] == "success") {
                alert(res['message'])
                window.location.reload();
            }

        }
    })
}

page = 1
size = 10
function showCodeReviewRequestList() {
    let display = $('#codeReviewRequestListDiv').css("display")
    if (display == "flex") {
        $('#codeReviewRequestListDiv').css({"display": "none"})
        return;
    }
    $('#codeReviewRequestListDiv').empty();
    $('#codeReviewRequestListDiv').show();
    $.ajax({
        type: "GET",
        url: `/reviews?page=${page}&size=${size}`,
        success: function(res) {
            let reviews = res['reviews']
            for (let i=0; i<reviews.length; i++) {

                let tmp_html = `<div class="card-header">${reviews[i].language}</div>
                                <div class="card-body">
                                    <h5 class="card-title">${reviews[i].title}</h5>
                                    <p class="card-text">${reviews[i].comment}</p>
                                    <a href="#" class="btn btn-primary">상세코드</a>
                                </div>`
                $('#codeReviewRequestListDiv').append(tmp_html);
            }
        }
    })
}

function showRequestedReviewList() {
    let display = $('#codeReviewRequestListDiv').css("display")
    if (display == "flex") {
        $('#codeReviewRequestListDiv').css({"display": "none"})
        return;
    }
    $('#codeReviewRequestListDiv').empty();
    $('#codeReviewRequestListDiv').show();

    $.ajax({
        type: "GET",
        url: `/reviewer/review?page=1&size=10`,
        success: function (res) {
            let reviews = res['reviews'];
            for (let i=0; i<reviews.length; i++) {
                let tmp_html = `<div class="card-header">${reviews[i].language}</div>
                                <div class="card-body">
                                    <h5 class="card-title">${reviews[i].title}</h5>
                                    <p class="card-text">${reviews[i].comment}</p>
                                    <a href="#" class="btn btn-primary">상세코드</a>
                                </div>`
                $('#codeReviewRequestListDiv').append(tmp_html);
            }
        }
    })
}

function openDetailModal(id) {

    $.ajax({
        type: "GET",
        url: `/review/reviewId=${id}`,
        success: function(res) {
            console.log(res);
        }
    })
}



// ajax 요청시 token이 있다면 헤더에 추가하도록 설정
$.ajaxPrefilter(function( options, originalOptions, jqXHR ) {
    if (sessionStorage.getItem('mytoken') != null) {
        jqXHR.setRequestHeader('Authorization', 'Bearer ' + sessionStorage.getItem('mytoken'));
    }
});

function clearCodeRequestForm() {
    $('#codeReviewTitle').val('')
    $('#codeReviewCode').val('')
    $('#codeReviewComment').val(''
    )
}