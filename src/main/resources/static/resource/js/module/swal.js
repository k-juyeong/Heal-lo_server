export default class sweetalert {

    //로그인 알림
    checkLogin(callBack) {
        Swal.fire({
            title: '로그인이 필요합니다',
            text: "로그인 하시겠습니까?",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '로그인하러 가기',
            cancelButtonText: '취소'
        }).then((result) => {
            if (result.isConfirmed) {
                callBack();
            }
        })
    }

    //즐겨찾기 목록 이동
    redirectBookmarkList() {
        Swal.fire({
            title: '즐겨찾기 추가 완료!',
            text: "즐겨찾기 목록에 추가되었습니다.",
            icon: 'success',
            position: 'top-end',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '목록이동',
            cancelButtonText: '계속보기'
        }).then((result) => {
            if (result.isConfirmed) {
                window.location.href = `#`;
            }
        })
    }

    //삭제 확인 알림
    checkDeleteAlert(callBack) {
        Swal.fire({
            title: '삭제하시겠습니까?',
            text: "삭제 결과는 되돌릴 수 없습니다.",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '네 삭제하겠습니다.',
            cancelButtonText: '아니요'
        }).then((result) => {
            if (result.isConfirmed) {
                Swal.fire(
                    '삭제를 완료했습니다.',
                )
                callBack();
            }
        })
    }

}


