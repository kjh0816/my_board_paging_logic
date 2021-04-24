fun main() {

    var articleLength = 100  // 게시물 수 (index = [0] ~ [99])
    val itemCountInAPage = 15 // 한 페이지에 보여줄 게시물 수
    var extraPage = 0         // 잔여 페이지
    var extraArticles = 0     // 잔여 게시물들
    var pageToSubtract = itemCountInAPage  // endIndex를 구하기 위해 startIndex에서 빼 줄 값

    if(articleLength % itemCountInAPage != 0){
        extraPage = 1
        extraArticles = articleLength - (articleLength / itemCountInAPage)*itemCountInAPage

    }
    val totalPage = (articleLength / itemCountInAPage) + extraPage // 총 페이지 수(=마지막 페이지)

    val page = 5 // 6번 페이지를 요청했을 때
    if (page == totalPage) {
        pageToSubtract = extraArticles
    }

    val startIndex =(totalPage-page) * itemCountInAPage + extraArticles - 1
    val endIndex = startIndex - pageToSubtract + 1



    // 최신 글이 위에 와야하므로, downTo로 뒤에서 부터 뺴와야함.
    // 7번일 경우, 보여줘야할 게시물 = 10개 (1번 게시물 ~ 9번 게시물 = index[9] ~ index[0]
    // 6번일 경우, 보여줘야할 게시물 = 15개 (10번 게시물 ~ 24번 게시물 = index[24] ~ index[10]
    // 5번일 경우, 보여줘야할 게시물 = 15개 (25번 게시물 ~ 39번 게시물 = index[39] ~ index[25]



    println("startIndex: $startIndex")
    println("endIndex: $endIndex")

}