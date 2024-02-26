const SERVER_URL = "http://localhost:8080"
const DEFAULT_CATEGORY_POST_ALL = "Articles"

const paginationBarPageSize = 10
const itemPerPage = 10
let currentPage = 1

// 초기화
document.addEventListener("DOMContentLoaded", function () {
    loadCategories()
    loadPosts(DEFAULT_CATEGORY_POST_ALL)
    renderPagination(100)
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
        loadPosts(categoryName)
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
    const pagination = document.getElementById("pagination");
    pagination.innerHTML = "";

    if(currentPage > paginationBarPageSize) {
        const prevBtn = document.createElement("button");
        prevBtn.textContent = "이전";
        prevBtn.addEventListener("click", function() {
            if (currentPage > 1) {
                currentPage -= paginationBarPageSize;
                if (currentPage < 1) {
                    currentPage = 1;
                }
                fetchData("/api/posts", function(data) {
                    renderPosts(data);
                });
            }
        });
        pagination.appendChild(prevBtn);
    }

    for (let i = 1; i <= totalPages; i++) {
        const button = document.createElement("button");
        button.textContent = i;
        button.addEventListener("click", function() {
            currentPage = parseInt(this.textContent);
            fetchData("/api/posts", function(data) {
                renderPosts(data);
            });
        });
        pagination.appendChild(button);
    }

    const startPageIndex = currentPage - (currentPage % paginationBarPageSize)
    if(startPageIndex + paginationBarPageSize <= totalPages - 1)
    const nextBtn = document.createElement("button");
    nextBtn.textContent = "다음";
    nextBtn.addEventListener("click", function() {
        if (currentPage < totalPages) {
            currentPage += paginationBarPageSize;
            if (currentPage > totalPages) {
                currentPage = totalPages;
            }
            fetchData("/api/posts", function(data) {
                renderPosts(data);
            });
        }
    });
    pagination.appendChild(nextBtn);
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