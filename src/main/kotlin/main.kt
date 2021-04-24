import java.text.SimpleDateFormat


// article write , article modify (id) / article delete (id) / article list a (id) / article detail (id)
fun main() {

    makeTestArticles()

    // 1) while loop 안에 when 명령어 망라
    // 2) Article 클래스 생성, Article 담을 mutableList 생성, articleLastId 변수 선언

    loop@ while (true) {
        println("명령어: ")

        val command = readLineTrim()

        when {

            command == "system exit" -> {
                println("프로그램을 종료합니다.")
                break
            }
            command == "article write" -> {

                println("제목: ")
                val title = readLineTrim()
                println("내용: ")
                val body = readLineTrim()

                val id = addArticle(title, body)

                println("${id}번 게시물이 작성되었습니다.")

            }
            command.startsWith("article modify ") -> {
                val id = command.trim().split(" ")[2].toInt()

                val articleToModify = getArticleById(id)

                if (articleToModify == null) {
                    println("${id}번 게시물은 존재하지 않습니다.")
                    continue
                }

                println("새 제목: ")
                articleToModify.title = readLineTrim()
                println("새 내용: ")
                articleToModify.body = readLineTrim()
                articleToModify.updateDate = Util.getNowDateStr()

                println("${id}번 게시물이 수정되었습니다.")

            }
            command.startsWith("article delete ") -> {
                val id = command.trim().split(" ")[2].toInt()

                val articleToDelete = getArticleById(id)

                if (articleToDelete == null) {
                    println("${id}번 게시물은 존재하지 않습니다.")
                    continue
                }

                articles.remove(articleToDelete)

                println("${id}번 게시물이 삭제되었습니다.")
            }
            command.startsWith("article detail ") -> {
                val id = command.trim().split(" ")[2].toInt()

                val article = getArticleById(id)
                if (article == null) {
                    println("${id}번 게시물은 존재하지 않습니다.")
                    continue
                }

                println("번호   : ${article.id}")
                println("제목   : ${article.title}")
                println("내용   : ${article.body}")
                println("작성날짜: ${article.regDate}")
                println("갱신날짜: ${article.updateDate}")

            }
            command.startsWith("article list ") -> {
                val choice = command.trim().split(" ")
                var page = 1
                var searchKeyword = ""
                if (choice.size == 3) {
                    page = command.trim().split(" ")[2].toInt()
                } else if (choice.size == 4) {
                    page = command.trim().split(" ")[2].toInt()
                    searchKeyword = command.trim().split(" ")[2]
                }

                val itemCountInAPage = 15 // Front에서 back으로 넘겨줄 데이터(한 페이지에 몇 개를 표시할 것인지)

                val filteredArticles = getFilteredArticles(searchKeyword, page, itemCountInAPage)

                println("번호 / 작성날짜 / 제목")
                for (i in filteredArticles.lastIndex downTo 0) {
                    println("${filteredArticles[i].id} / ${filteredArticles[i].regDate} / ${filteredArticles[i].title}")
                }


            }
            else -> {
                println("`${command}`은(는) 존재하지 않는 명령어입니다.")
            }


        }
    }


}


/* 게시물 관련 시작 */

var articleLastId = 0
val articles: MutableList<Article> = mutableListOf<Article>()


data class Article(
    val id: Int,
    var title: String,
    var body: String,
    val regDate: String,
    var updateDate: String
)

fun addArticle(title: String, body: String): Int {

    val id = articleLastId + 1
    articleLastId = id

    val regDate = Util.getNowDateStr()
    val updateDate = Util.getNowDateStr()

    val article = Article(id, title, body, regDate, updateDate)

    articles.add(article)

    return id

}

fun makeTestArticles() {
    for (i in 1..100) {
        val title = "제목_${i}"
        val body = "제목_${i}"
        addArticle(title, body)
    }

}

fun getArticleById(id: Int): Article? {
    for (article in articles) {
        if (article.id == id) {
            return article
        }
    }
    return null
}

fun getFilteredArticles(searchKeyword: String, page: Int, itemCountInAPage: Int): List<Article> {

    var filteredArticle = mutableListOf<Article>()  // 필터링한 게시물들을 담을 리스트


    // searchKeyword에 값이 존재하는 경우(제목 검색)의 필터링
    if (searchKeyword.isNotEmpty()) {
        for (article in articles) {
            if (article.title.contains(searchKeyword)) {
                filteredArticle.add(article)
            }
        }

        if (filteredArticle.size == 0) {
            println("${searchKeyword}에 해당하는 제목이 없습니다.")
        }
    } else if (searchKeyword.isEmpty()) {


        /* 페이징 처리 로직(back) 시작 */

        var articlesSize = articles.size  // 총 게시물 수 filteredArticles.lastIndex
        var extraPage = 0         // 잔여 페이지
        var extraArticles = 0     // 잔여 게시물들
        var pageToSubtract = itemCountInAPage  // endIndex를 구하기 위해 startIndex에서 빼 줄 값

        if (articlesSize % itemCountInAPage != 0) {   // 잔여 페이지, 잔여 게시물들을 처리하기 위한 필터링
            extraPage = 1
            extraArticles = articlesSize - (articlesSize / itemCountInAPage) * itemCountInAPage
        }
        val totalPage = (articlesSize / itemCountInAPage) + extraPage // 총 페이지 수(=마지막 페이지)

        if (page > totalPage) {
            println("해당 페이지가 존재하지 않습니다.")

        }


        if (page == totalPage) {  // 마지막 페이지일 경우 출력할 인덱스값 조정
            pageToSubtract = extraArticles
        }
        if(page <= totalPage){   // 페이지가 존재할 경우에만 실행되도록.

            // 최신 글이 위에 와야하므로, downTo로 뒤에서부터 빼와야함.
            var startIndex = (totalPage - page) * itemCountInAPage + extraArticles - 1
            var endIndex = startIndex - pageToSubtract + 1


            /* 페이징 처리 로직(back) 끝 */

            for (i in startIndex downTo endIndex) {
                filteredArticle.add(articles[i])

            }


        }
    }
    return filteredArticle


}

/* 게시물 관련 끝 */
/* 유틸 시작 */


fun readLineTrim() = readLine()!!.trim()


object Util {


    fun getNowDateStr(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        return dateFormat.format(System.currentTimeMillis())
    }
}


/* 유틸 끝 */