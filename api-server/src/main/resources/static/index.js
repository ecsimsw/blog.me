const DEFAULT_CATEGORY_POST_ALL = "Articles"

const serverUrl = "http://localhost:8080"

// 초기화
document.addEventListener("DOMContentLoaded", function () {
    loadCategories();
    loadPosts(DEFAULT_CATEGORY_POST_ALL);
});

// 카테고리 목록을 조회하여 그리는 함수
function loadCategories() {
    const categoryList = document.getElementById("category-list");
    addCategory(categoryList, DEFAULT_CATEGORY_POST_ALL);
    fetchData(serverUrl + "/api/category", function (categories) {
        categories.forEach(function (category) {
            addCategory(categoryList, category.name);
        });
    })
}

// 카테고리 하위 내용을 추가하는 함수
function addCategory(categoryList, categoryName) {
    const li = document.createElement("li");
    const a = document.createElement("a");
    a.className = "category-name"
    a.textContent = categoryName;
    a.addEventListener('click', function () {
        let contentTitle = document.getElementById("content-title");
        contentTitle.textContent = categoryName
        loadPosts(categoryName)
    })
    li.appendChild(a);
    categoryList.appendChild(li)
}

// 게시글 목록을 그리는 함수
function updatePosts(posts) {
    const oldPostList =document.querySelectorAll('#post-list li')
    oldPostList.forEach(oldPost => {
        oldPost.parentNode.removeChild(oldPost);
    });

    const postList = document.getElementById("post-list");
    posts.forEach(function (post) {
        const li = document.createElement("li");
        const a = document.createElement("a");
        const h3 = document.createElement("h3");
        const p = document.createElement("p");

        a.href = post.path + "/" + post.path + ".html"; // 각 글에 대한 링크
        a.textContent = post.title;
        h3.appendChild(a);
        li.appendChild(h3);
        li.appendChild(p);
        postList.appendChild(li);
    })
}

// category 에 해당하는 글을 조회하여 그리는 함수
function loadPosts(categoryName) {
    if (categoryName === DEFAULT_CATEGORY_POST_ALL) {
        fetchData(serverUrl + "/api/article", updatePosts)
    } else {
        fetchData(serverUrl + "/api/article?category=" + categoryName, updatePosts)
    }
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