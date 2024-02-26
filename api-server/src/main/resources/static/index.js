const serverUrl = "http://localhost:8080"

document.addEventListener("DOMContentLoaded", function () {
    loadCategories();
    loadPosts();
});

function loadCategories() {
    const categoryList = document.getElementById("category-list");
    const li = document.createElement("li");
    const a = document.createElement("a");
    a.className = "category-name"
    a.textContent = "Articles";
    a.addEventListener('click', function () {
        let contentTitle = document.getElementById("content-title");
        contentTitle.textContent = a.textContent
        loadPosts()
    })
    li.appendChild(a);
    categoryList.appendChild(li)

    fetchData(serverUrl + "/api/category", function (categories) {
        categories.forEach(function (category) {
            const li = document.createElement("li");
            const a = document.createElement("a");
            a.className = "category-name"
            a.textContent = category.name;
            a.addEventListener('click', function () {
                let contentTitle = document.getElementById("content-title");
                contentTitle.textContent = category.name
                loadPosts(category.name)
            })
            li.appendChild(a);
            categoryList.appendChild(li);
        });
    })
}

function loadPosts(categoryName) {
    if (categoryName === undefined) {
        fetchData(serverUrl + "/api/article",  function (posts) {
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
        })
        return
    }
    fetchData(serverUrl + "/api/article?category=" + categoryName, function(posts) {
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
    })
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