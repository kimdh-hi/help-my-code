
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
        $('#myReviewRequestListBtn').show()
    } else { // 인증이 되지 않은 경우
        $('#signinBtn').show()
        $('#signupBtn').show()
        $('#logoutBtn').hide()

        $('#reviewRequestBtn').hide()
        $('#reviewAnswerBtn').hide()
        $('#myReviewRequestListBtn').hide()
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
    let title = $('#codeReviewRequestTitle').val()
    let code = $('#codeReviewRequestCode').val()
    let comment = $('#codeReviewRequestComment').val()
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
                                    <a onclick="showReviewDetailModal('${reviews[i].id}', false)" class="btn btn-primary">상세코드</a>
                                </div>`
                $('#codeReviewRequestListDiv').append(tmp_html);
            }
        }
    })
}

// 나에게 요청된 코드리뷰목록 보기
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
            console.log(res)

            let reviews = res['reviews'];
            for (let i=0; i<reviews.length; i++) {
                let statusBadge = getStatusBadge(reviews[i].status)
                console.log(statusBadge)
                let tmp_html = `<div class="card-header">${reviews[i].language} ${statusBadge}</div>
                                <div class="card-body">
                                    <h5 class="card-title">${reviews[i].title}</h5>
                                    <p class="card-text">${reviews[i].comment}</p>
                                    <a onclick="showReviewDetailModal('${reviews[i].id}', true)" class="btn btn-primary">상세코드</a>
                                </div>`
                $('#codeReviewRequestListDiv').append(tmp_html);
            }
        }
    })
}

// 내가 요청한 코드리뷰목록 보기
function showMyReviewRequestList() {
    let display = $('#codeReviewRequestListDiv').css("display")
    if (display == "flex") {
        $('#codeReviewRequestListDiv').css({"display": "none"})
        return;
    }
    $('#codeReviewRequestListDiv').empty();
    $('#codeReviewRequestListDiv').show();

    $.ajax({
        type: "GET",
        url: `/user/reviews?page=1&size=10`,
        success: function (res) {
            let reviews = res['reviews'];
            for (let i=0; i<reviews.length; i++) {
                let tmp_html = `<div class="card-header">${reviews[i].language}</div>
                                <div class="card-body">
                                    <h5 class="card-title">${reviews[i].title}</h5>
                                    <p class="card-text">${reviews[i].comment}</p>
                                    <a onclick="showReviewDetailModal('${reviews[i].id}', false)" class="btn btn-primary">상세코드</a>
                                </div>
                                <div class="card-footer">
                                    <button onclick="" type="button" class="btn btn-warning">수정</button>
                                    <button onclick="deleteRequest('${reviews[i].id}')" type="button" class="btn btn-danger">삭제</button>
                                </div>`
                $('#codeReviewRequestListDiv').append(tmp_html);
            }
        }
    })
}


function deleteRequest(id) {
    $.ajax({
        type: "DELETE",
        url: `/review?id=${id}`,
        success: function(res) {
            console.log(res)
            alert(res['message'])
            showMyReviewRequestList()
        }
    })
}

function editRequest(id) {

}

function showReviewDetailModal(id, isReviewer) {
    $('#reviewDetailBody').empty()
    $('#modalReviewTitle').html('')
    let title;
    let code;
    let comment;

    $.ajax({
        type: "GET",
        url: `/review?id=${id}`,
        success: function(res) {

            title = res.title;
            code = res.code;
            comment = res.comment;

            if (isReviewer) {
                console.log('showDetail: ' + title);
                console.log('isReviewer: '+isReviewer)
                $('#reviewDetailFooter').empty()
                let tmp_btn = `<button class="btn btn-info" onclick="showReviewForm('${id}', '${title}', '${code}', '${comment}')">리뷰하기</button>`
                $('#reviewDetailFooter').append(tmp_btn)
                $('#reviewDetailFooter').append(
                    `<button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>`
                )
            }


            $('#modalReviewTitle').html(title)
            let tmp_html = `<div class="mb-3" id="reviewCode">
                                ${code}
                            </div>
                            <div class="mb-3" id="reviewComment">
                                ${comment}
                            </div>`

            $('#reviewDetailBody').append(tmp_html)
        }
    })

    $('#codeReviewDetailModal').modal('show')
}

function showReviewForm(id, title, code, comment) {
    $('#requestedReviewModalHeader').empty();
    $('#requestedReviewModalBody').empty();
    $('#requestedReviewModalComment').empty();

    $('#requestedReviewModalHeader').html(title);
    $('#requestedReviewModalBody').html(code)
    $('#requestedReviewModalComment').html(comment)

    let tmp_btn = `<button type="button" class="btn btn-secondary" onclick="addReview('${id}')">완료</button`
    $('#reviewModalFooter').append(tmp_btn)


    $('#codeReviewDetailModal').modal('hide')
    $('#codeReviewModal').modal('show')
}

function addReview(id) {
    let title = $('#codeReviewTitle').val()
    let code = $('#codeReviewCode').val()
    let comment = $('#codeReviewComment').val()

    let data = {
        "title": title,
        "code": code,
        "comment": comment
    }

    $.ajax({
        type: "POST",
        url: `/reviewer/review?id=${id}`,
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify(data),
        success: function(res) {
            console.log(res)
            if (res['result'] == "success") {
                alert(res['message'])
                window.location.reload()
            }
        }
    })
}

function getStatusBadge(status) {
    let statusBadge;
    if (status == "REQUESTED") {
        statusBadge = `<span class="badge bg-primary">요청됨</span>`
    } else if (status =="DONE") {
        statusBadge = `<span class="badge bg-success">완료됨</span>`
    } else if (status == "REJECT") {
        statusBadge = `<span class="badge bg-secondary">거절됨</span>`
    }

    return statusBadge;
}

// ajax 요청시 token이 있다면 헤더에 추가하도록 설정
$.ajaxPrefilter(function( options, originalOptions, jqXHR ) {
    if (sessionStorage.getItem('mytoken') != null) {
        jqXHR.setRequestHeader('Authorization', 'Bearer ' + sessionStorage.getItem('mytoken'));
    }
});

function clearCodeRequestForm() {
    $('#codeReviewRequestTitle').val('')
    $('#codeReviewRequestCode').val('')
    $('#codeReviewRequestComment').val('')
}