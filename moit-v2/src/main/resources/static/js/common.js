function getCsrfHeaders() {
    const token = document.querySelector('meta[name="_csrf"]').content;
    const header = document.querySelector('meta[name="_csrf_header"]').content;
console.log(document.querySelector('meta[name="_csrf"]'));
console.log(document.querySelector('meta[name="_csrf_header"]'));
    return {
        "Content-Type": "application/json",
        [header]: token
    };
}