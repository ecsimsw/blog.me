const SERVER_URL = "http://localhost:8080"
const DEFAULT_CATEGORY_POST_ALL = "Articles"

const paginationBarPageSize = 10
const itemPerPage = 10
let currentPage = 1

// 초기화
document.addEventListener("DOMContentLoaded", function () {
    loadCategories()
    loadPosts(DEFAULT_CATEGORY_POST_ALL)
    renderPagination(1000)
    document.getElementById('page-btn-'+ currentPage)
        .classList.add('selected');
});

// 카테고리 목록을 조회하여 그리는 함수
function loadCategories() {
    const categoryList = document.getElementById("category-list")
    fetchData(SERVER_URL + "/api/article/count", function (count) {
        addCategory(categoryList, DEFAULT_CATEGORY_POST_ALL, count)
        fetchData(SERVER_URL + "/api/category", function (categories) {
            categories.forEach(function (category) {
                addCategory(categoryList, category.name, category.numberOfPosts)
            });
        })
    })
}

// 카테고리 하위 내용을 추가하는 함수
function addCategory(categoryList, categoryName, numberOfPosts) {
    const li = document.createElement("li")
    const a = document.createElement("a")
    a.className = "category-name"
    a.textContent = categoryName + "(" + numberOfPosts + ")"
    a.addEventListener('click', function () {
        let contentTitle = document.getElementById("content-title")
        contentTitle.textContent = categoryName
        currentPage = 1
        loadPosts(categoryName)
        renderPagination(numberOfPosts)
    })
    li.appendChild(a)
    categoryList.appendChild(li)
}

// 게시글 목록을 그리는 함수
function renderPosts(posts) {
    const postList = document.getElementById("post-list")
    postList.innerHTML = ""
    posts.forEach(function (post) {
        const li = document.createElement("li")
        const a = document.createElement("a")
        const h3 = document.createElement("h3")
        const p = document.createElement("p")
        a.href = post.path
        a.className = "post-list-item"
        a.textContent = post.title
        h3.appendChild(a)
        li.appendChild(h3)
        li.appendChild(p)
        postList.appendChild(li)
    })
}

// category 에 해당하는 글을 조회하여 그리는 함수
function loadPosts(categoryName) {
    if (categoryName === DEFAULT_CATEGORY_POST_ALL) {
        fetchData(SERVER_URL + "/api/article", renderPosts)
    } else {
        fetchData(SERVER_URL + "/api/article?category=" + categoryName, renderPosts)
    }
}

// 페이지네이션 생성
function renderPagination(totalItems) {
    const totalPages = Math.ceil(totalItems / itemPerPage);
    const startPageIndex = Math.min(currentPage, currentPage - (currentPage % paginationBarPageSize) + 1)
    const endPageIndex = Math.min(startPageIndex + paginationBarPageSize, totalPages)
    const pagination = document.getElementById("pagination");
    pagination.innerHTML = "";

    if (startPageIndex > paginationBarPageSize) {
        const prevBtn = document.createElement("li");
        prevBtn.textContent = "<";
        prevBtn.className = "page-btn";
        prevBtn.addEventListener("click", function () {
            currentPage = startPageIndex - paginationBarPageSize
            renderPagination(totalItems)
        });
        pagination.appendChild(prevBtn);
    }

    for (let i = startPageIndex; i < endPageIndex; i++) {
        const button = document.createElement("li")
        button.textContent = i;
        button.className = "page-btn";
        button.id = "page-btn-" + i;
        button.addEventListener("click", function () {
            if(document.getElementsByClassName("selected").length > 0) {
                document.getElementsByClassName("selected")[0]
                    .classList.remove("selected")
            }
            document.getElementById('page-btn-'+ i)
                .classList.add('selected')
            fetchData("/api/posts", function (data) {
                renderPosts(data);
            });
        });
        pagination.appendChild(button);
    }

    if (startPageIndex + paginationBarPageSize < totalPages) {
        const nextBtn = document.createElement("li");
        nextBtn.textContent = ">";
        nextBtn.className = "page-btn";
        nextBtn.addEventListener("click", function () {
            currentPage = startPageIndex + paginationBarPageSize
            renderPagination(totalItems)
        });
        pagination.appendChild(nextBtn);
    }

    if(document.getElementsByClassName("selected").length > 0) {
        document.getElementsByClassName("selected")[0]
            .classList.remove("selected")
    }
    document.getElementById('page-btn-'+ currentPage)
        .classList.add('selected')
}

// 서버에서 데이터를 받아오는 함수
function fetchData(url, callback) {
    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => callback(data))
        .catch(error => {
            console.log(error)
        });
}