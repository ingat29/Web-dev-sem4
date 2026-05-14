$(document).ready(function() {

    $('#openModalBtn').click(function() {
        $('#overlay').fadeIn();
        $('#modal').fadeIn();

        $('#mainForm input').prop('disabled', true);
        $('#openModalBtn').prop('disabled', true);
    });

    $('#closeModalBtn').click(function() {
        let val1 = $('#mod1').val();
        let val2 = $('#mod2').val();
        let val3 = $('#mod3').val();
        let val4 = $('#mod4').val();

        let concatenatedResult = val1 + "" + val2 + "" + val3 + "" + val4;

        $('#mainResult').val(concatenatedResult);

        $('#modal').fadeOut();
        $('#overlay').fadeOut();

        $('#mainForm input').prop('disabled', false);
        $('#openModalBtn').prop('disabled', false);

        $('#modalForm input').val('');
    });

});