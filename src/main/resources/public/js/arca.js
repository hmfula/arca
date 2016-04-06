$(document).ready(function() {
    $.ajax({
        url:"http://localhost:8080/api/causes"
    }).then(function(causes) {
        causes.forEach(
            function(cause){
            $('.cause-id').append(cause.id);
            $('.cause-name').append(cause.name);
            $('.cause-frequency').append(cause.frequency);
            $('.cause-total').append(cause.total);
            $('.cause-description').append(cause.description);

        });
    });
});