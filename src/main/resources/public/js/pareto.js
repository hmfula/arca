/**
 * Created by harry on 6.4.2016.
 */

$(document).ready(function() {
    $.ajax({
        url:"http://localhost:8080/api/causes"
    }).then(function(causes) {
        causes.forEach(
            function(cause){
                $('.cause-id').append(cause.id);
                $('.cause-name').append(cause.text);
                $('.cause-frequency').append(cause.frequency);
            });
    });
});
