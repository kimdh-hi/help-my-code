$(document).ready(function () {

    loginCheck()
    authCheck()

    $("#signup-isReviewer").change(function () {
        if ($("#signup-isReviewer").is(":checked")) {
            $('#language-check-box').show()
        } else {
            $('#language-check-box').hide()
        }
    })

    $("#codeReviewRequestLanguageSelectBox").on("change", function () {
        let language = $(this).val().toString()

        $.ajax({
            type: "GET",
            url: `/review/language/user?language=${language}`,
            success: function (res) {
                $('#codeReviewRequestReviewerSelectBox').empty()
                let tmp_html = "<option selected>리뷰어 선택</option>"
                $('#codeReviewRequestReviewerSelectBox').append(tmp_html)
                for (let i = 0; i < res.length; i++) {
                    let tmp_html = `<option value="${res[i]['userId']}">${res[i]['username']}</option>`
                    $('#codeReviewRequestReviewerSelectBox').append(tmp_html)
                }
                $('#reviewerSelectDiv').show();
            }
        })
    })

    $("#codeReviewRequestReviewerSelectBox").on("change", function () {
        let userId = $(this).val()
        $('#requestCodeReviewBtn').show()

    })

    // ========================================
    // 코드리뷰 요청 전체목록 처리상태 필터링 이벤트
    // ========================================
    $('#reviewRequestStatusSelectBox').off().on("change", function () {
        let status = $(this).val()
        $('#reviewRequestListDiv').empty()
        $.ajax({
            type: "GET",
            url: `/reviews?page=1&size=10&status=${status}`,
            success: function (res) {
                console.log(res)
                $('#reviewRequestListDiv').show();

                let reviews = res['reviews']
                if (reviews.length > 0) {
                    for (let i = 0; i < reviews.length; i++) {
                        let tmp_html = makeReviewRequestCard(reviews[i])
                        $('#reviewRequestListDiv').append(tmp_html);
                    }
                } else {
                    let tmp_html = `<p>조회된 결과가 없습니다.</p>`
                    $('#reviewRequestListDiv').append(tmp_html);
                }
            }
        })
        $('#reviewRequestListDivBox').show();
    })

    // ========================================
    // 내가 요청한 코드리뷰 전체목록 처리상태 필터링 이벤트
    // ========================================
    $('#myReviewRequestStatusSelectBox').off().on("change", function () {
        let status = $(this).val()
        $('#myReviewRequestListDiv').empty()
        $.ajax({
            type: "GET",
            url: `/user/reviews?page=1&size=10&status=${status}`,
            success: function (res) {
                console.log(res)
                $('#myReviewRequestListDiv').show();

                let reviews = res['reviews']
                if (reviews.length > 0) {
                    for (let i = 0; i < reviews.length; i++) {
                        let tmp_html = makeReviewRequestCard(reviews[i])
                        $('#myReviewRequestListDiv').append(tmp_html);
                    }
                } else {
                    let tmp_html = `<p>조회된 결과가 없습니다.</p>`
                    $('#myReviewRequestListDiv').append(tmp_html);
                }
            }
        })
        $('#myReviewRequestListDivBox').show();
    })

    // ========================================
    // 내에게 요청된 코드리뷰 전체목록 처리상태 필터링 이벤트
    // ========================================
    $('#reviewRequestedStatusSelectBox').off().on("change", function () {
        let status = $(this).val()
        $('#reviewRequestedListDiv').empty()
        $.ajax({
            type: "GET",
            url: `/reviewer/reviews?page=1&size=10&status=${status}`,
            success: function (res) {
                $('#reviewRequestedListDiv').show();

                let reviews = res['reviews']
                if (reviews.length > 0) {
                    for (let i = 0; i < reviews.length; i++) {
                        let tmp_html = makeReviewRequestCard(reviews[i])
                        $('#reviewRequestedListDiv').append(tmp_html);
                    }
                } else {
                    let tmp_html = `<p>조회된 결과가 없습니다.</p>`
                    $('#reviewRequestedListDiv').append(tmp_html);
                }
            }
        })
        $('#reviewRequestedListDivBox').show();
    })
})

// ========================================
// 코드리뷰 요청 폼 모달
// ========================================
function showCodeReviewRequestForm() {
    let display = $('#codeReviewRequestForm').css("display")

    hideOtherReviewListBox("codeReviewRequestForm");

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

// ========================================
// 코드리뷰 요청
// ========================================
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
        success: function (res) {
            if (res['result'] == "success") {
                alert(res['message'])
                window.location.reload();
            }

        }
    })
}

// ========================================
// 모든 코드리뷰요청 목록
// ========================================
function showCodeReviewRequestList() {
    let display = $('#reviewRequestListDivBox').css("display")
    if (display == "block") {
        $('#reviewRequestListDivBox').hide()
        return;
    }

    hideOtherReviewListBox('reviewRequestListDivBox')

    $('#reviewRequestListDivBox').show()
    $('#reviewRequestListDiv').empty();

    $.ajax({
        type: "GET",
        url: `/reviews?page=1&size=10&status=ALL`,
        success: function (res) {
            let reviews = res['reviews']
            if (reviews.length > 0) {
                for (let i = 0; i < reviews.length; i++) {
                    let tmp_html = makeReviewRequestCard(reviews[i], false)
                    $('#reviewRequestListDiv').append(tmp_html);
                }
            } else {
                let tmp_html = `<p>조회된 결과가 없습니다.</p>`
                $('#reviewRequestListDiv').append(tmp_html);
            }
        }
    })

    $('#reviewRequestListDiv').show();

}

// ========================================
// 나에게 요청된 코드리뷰목록 보기
// ========================================
function showRequestedReviewList() {
    let display = $('#reviewRequestedListDivBox').css("display")
    if (display == "block") {
        $('#reviewRequestedListDivBox').hide()
        return;
    }

    hideOtherReviewListBox('reviewRequestedListDivBox')

    $('#reviewRequestedListDivBox').show()
    $('#reviewRequestedListDiv').empty();

    $.ajax({
        type: "GET",
        url: `/reviewer/reviews?page=1&size=10&status=ALL`,
        success: function (res) {
            let reviews = res['reviews']
            if (reviews.length > 0) {
                for (let i = 0; i < reviews.length; i++) {
                    let tmp_html = makeReviewRequestCard(reviews[i], true)
                    $('#reviewRequestedListDiv').append(tmp_html);
                }
            } else {
                let tmp_html = `<p>조회된 결과가 없습니다.</p>`
                $('#reviewRequestedListDiv').append(tmp_html);
            }
        }
    })
    $('#reviewRequestedListDiv').show();
}

// ========================================
// 내가 요청한 코드리뷰목록 보기
// ========================================
function showMyReviewRequestList() {
    let display = $('#myReviewRequestListDivBox').css("display")
    if (display == "block") {
        $('#myReviewRequestListDivBox').hide()
        return;
    }

    hideOtherReviewListBox('myReviewRequestListDivBox')

    $('#myReviewRequestListDivBox').show()
    $('#myReviewRequestListDiv').empty();

    $.ajax({
        type: "GET",
        url: `/user/reviews?page=1&size=10&status=ALL`,
        success: function (res) {
            let reviews = res['reviews']
            if (reviews.length > 0) {
                for (let i = 0; i < reviews.length; i++) {
                    let tmp_html = makeMyReviewRequestCard(reviews[i])
                    $('#myReviewRequestListDiv').append(tmp_html);
                }
            } else {
                let tmp_html = `<p>조회된 결과가 없습니다.</p>`
                $('#myReviewRequestListDiv').append(tmp_html);
            }
        }
    })
    $('#myReviewRequestListDiv').show();
}


// ========================================
// 내가 요청한 코드리뷰 요청 삭제
// ========================================
function deleteRequest(id) {
    $.ajax({
        type: "DELETE",
        url: `/review?id=${id}`,
        success: function (res) {
            console.log(res)
            alert(res['message'])
            showMyReviewRequestList()
        }
    })
}

// ========================================
// 코드리뷰 요청 수정 (모달 폼)
// ========================================
function showEditRequestForm(id, title, code, comment) {

    $('#codeReviewUpdateTitle').val(title)
    $('#codeReviewUpdateCode').val(code)
    $('#codeReviewUpdateComment').val(comment)

    $('#reviewUpdateFooter').empty()
    let closeBtn = `<button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>`
    let updateBtn = `<button onclick="updateRequest('${id}')" type="button" class="btn btn-primary">Update</button>`
    $('#reviewUpdateFooter').append(closeBtn);
    $('#reviewUpdateFooter').append(updateBtn);

    $('#codeReviewUpdateModal').modal('show')
}

// ========================================
// 코드리뷰 요청 수정
// ========================================
function updateRequest(id) {
    let title = $('#codeReviewUpdateTitle').val()
    let code = $('#codeReviewUpdateCode').val()
    let comment = $('#codeReviewUpdateComment').val()

    let data = {
        "title": title,
        "code": code,
        "comment": comment
    }

    $.ajax({
        type: "PUT",
        url: `/review?id=${id}`,
        contentType: "application/json;charset=utf-8;",
        data: JSON.stringify(data),
        success: function (res) {
            console.log(res)
            if (res.result == "success") {
                alert(res.message)
                window.location.reload();
            }
        }
    })
}


// ========================================
// 코드리뷰요청 상세정보
// ========================================
function showReviewDetailModal(id, isReviewer) {
    $('#reviewDetailBody').empty()
    $('#modalReviewTitle').html('')
    let title;
    let code;
    let comment;

    $.ajax({
        type: "GET",
        url: `/review?id=${id}`,
        success: function (res) {

            title = res.title;
            code = res.code;
            comment = res.comment;

            if (isReviewer) {
                console.log('showDetail: ' + title);
                console.log('isReviewer: ' + isReviewer)
                $('#reviewDetailFooter').empty()
                let tmp_btn = `<button class="btn btn-info" onclick="showReviewForm('${id}', '${title}', '${code}', '${comment}')">리뷰하기</button>`
                $('#reviewDetailFooter').append(tmp_btn)
                $('#reviewDetailFooter').append(
                    `<button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>`
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

// ========================================
// 코드리뷰 폼 모달
// ========================================
function showReviewForm(id, title, code, comment) {
    $('#requestedReviewModalHeader').empty();
    $('#requestedReviewModalBody').empty();
    $('#requestedReviewModalComment').empty();

    $('#requestedReviewModalHeader').html(title);
    $('#requestedReviewModalBody').html(code)
    $('#requestedReviewModalComment').html(comment)

    let addBtn = `<button type="button" class="btn btn-primary" onclick="addReview('${id}')">완료</button`
    let rejectBtn = `<button type="button" class="btn btn-warn" onclick="rejectReview('${id}')">거절</button`
    $('#reviewModalFooter').append(addBtn)
    $('#reviewModalFooter').append(rejectBtn)

    $('#codeReviewDetailModal').modal('hide')
    $('#codeReviewModal').modal('show')
}

// ========================================
// 요청된 코드리뷰 처리
// ========================================
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
        success: function (res) {
            console.log(res)
            if (res['result'] == "success") {
                alert(res['message'])
                window.location.reload()
            }
        }
    })
}

// ========================================
// 요청된 코드리뷰 거절
// ========================================
function rejectReview(id) {

    $.ajax({
        type: "PUT",
        url: `/reviewer/review/reject?id=${id}`,
        success: function (res) {
            console.log(res);
            alert(res.message)
            window.location.reload();
        }
    })
}

// ========================================
// ajax 요청시 token이 있다면 헤더에 추가하도록 설정
// ========================================
$.ajaxPrefilter(function (options, originalOptions, jqXHR) {
    if (sessionStorage.getItem('mytoken') != null) {
        jqXHR.setRequestHeader('Authorization', 'Bearer ' + sessionStorage.getItem('mytoken'));
    }
});

//===============================================//
//===============================================//
//===============================================//

function makeMyReviewRequestCard(review) {
    let id = review.id;
    let title = review.title;
    let code = review.code;
    let comment = review.comment;
    let status = review.status;
    let language = review.language;

    let statusBadge = getStatusBadge(status)

    let tmp_html = `<div class="card-header">${language}</div>
                                <div class="card-body">
                                    <h5 class="card-title">${title} ${statusBadge}</h5>
                                    <p class="card-text">${comment}</p>
                                    <a onclick="showReviewDetailModal('${id}', false)" class="btn btn-primary">상세코드</a>
                                </div>
                                <div class="card-footer">
                                    <button onclick="showEditRequestForm('${id}', '${title}', '${code}','${comment}')" type="button" class="btn btn-warning">수정</button>
                                    <button onclick="deleteRequest('${id}')" type="button" class="btn btn-danger">삭제</button>
                                </div>`
    return tmp_html
}

function makeReviewRequestCard(review, isReviewer) {
    let statusBadge = getStatusBadge(review.status)
    let tmp_card = `<div class="card-header">${review.language} ${statusBadge}</div>
                            <div class="card-body">
                                <h5 class="card-title">${review.title}</h5>
                                <p class="card-text">${review.comment}</p>
                                <a onclick="showReviewDetailModal('${review.id}', ${isReviewer})" class="btn btn-primary">상세코드</a>
                            </div>`
    return tmp_card;

}

function clearCodeRequestForm() {
    $('#codeReviewRequestTitle').val('')
    $('#codeReviewRequestCode').val('')
    $('#codeReviewRequestComment').val('')
}

function getStatusBadge(status) {
    let statusBadge;
    if (status == "REQUESTED") {
        statusBadge = `<span class="badge bg-primary">요청됨</span>`
    } else if (status == "DONE") {
        statusBadge = `<span class="badge bg-success">완료됨</span>`
    } else if (status == "REJECT") {
        statusBadge = `<span class="badge bg-secondary">거절됨</span>`
    }

    return statusBadge;
}

function hideOtherReviewListBox(boxId) {
    if (boxId == "reviewRequestListDivBox") {
        $('#myReviewRequestListDivBox').hide()
        $('#reviewRequestedListDivBox').hide()
        $('#codeReviewRequestForm').hide()
    } else if (boxId == "myReviewRequestListDivBox") {
        $('#reviewRequestedListDivBox').hide()
        $('#reviewRequestListDivBox').hide()
        $('#codeReviewRequestForm').hide()
    } else if (boxId == "reviewRequestedListDivBox") {
        $('#reviewRequestListDivBox').hide()
        $('#myReviewRequestListDivBox').hide()
        $('#codeReviewRequestForm').hide()
    } else if (boxId == "codeReviewRequestForm") {
        $('#reviewRequestListDivBox').hide()
        $('#myReviewRequestListDivBox').hide()
        $('#reviewRequestedListDivBox').hide()
    }
}

function loginCheck() {
    // 인증이 된 경우
    if (sessionStorage.getItem("mytoken") != null) {
        $('#signinBtn').hide()
        $('#signupBtn').hide()
        $('#logoutBtn').show()

        $('#reviewRequestBtn').show()
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

function authCheck() {
    if (sessionStorage.getItem("myAuthority") != null) {
        if (sessionStorage.getItem("myAuthority") == "ROLE_USER") {
            $('#reviewAnswerBtn').hide()
        } else if (sessionStorage.getItem("myAuthority") == "ROLE_REVIEWER") {
            $('#reviewAnswerBtn').show()

        }

    }
}