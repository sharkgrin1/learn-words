$(".js-select2").select2({
    closeOnSelect: false,
    placeholder: " Группы",
    // allowHtml: true,
    allowClear: true,
    "language": {
        "noResults": function () {
            return "Нет результатов";
        }
    }
    // tags: true,
});