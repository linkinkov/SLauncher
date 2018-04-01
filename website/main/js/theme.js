$("input[id='password_no']").on("click", function() {

    $("#user_pass_mojang").addClass("hidden");
    $("#user_pass_ely").addClass("hidden");
    $("#pay-acc").hide()
    $("#no-pass").show()
    $('#user_nick').attr('placeholder',"Например, SLauncher")

    $("#account_no").show();
    $("#account_mojang").hide();
    $("#account_ely").hide();

});

$("input[id='password_mojang']").on("click", function() {

    $("#user_pass_no").addClass("hidden");
    $("#user_pass_mojang").removeClass("hidden").focus();
    $("#user_pass_ely").addClass("hidden");
    $("#pay-acc").show()
    $("#no-pass").hide()
    $('#user_nick').attr('placeholder',"Например, SLauncher@gmail.com")


    $("#account_no").hide();
    $("#account_mojang").show();
    $("#account_ely").hide();

});

$("input[id='password_ely']").on("click", function() {

    $("#user_pass_no").addClass("hidden");
    $("#user_pass_mojang").addClass("hidden");
    $("#user_pass_ely").removeClass("hidden").focus();
    $("#pay-acc").show()
    $("#no-pass").hide()
    $('#user_nick').attr('placeholder',"Например, SLauncher")

    $("#account_no").hide();
    $("#account_mojang").hide();
    $("#account_ely").show();

});

$("#manage_versions").on("click", function() {

    $('#modalVersions').modal('show');
    $("#versions").focus();

    return false;

});

$("#versions option[data-installed='true']").on("click", function() {

    if( $(this).attr("data-new-update") == 'true' ) {
        $("#button_update").show();
    } else {
        $("#button_update").hide();
    }

    $("#button_install").hide();
    $("#button_reinstall").show();
    $("#button_remove").show();

});

$("#versions option[data-installed='true']").first().click();
$("#versions option[data-installed='true']").on("click", function() {

    if( $(this).attr("data-new-update") == 'true' ) {
        $("#button_update").show();
    } else {
        $("#button_update").hide();
    }

    $("#button_install").hide();
    $("#button_reinstall").show();
    $("#button_remove").show();

});

$("#versions option[data-installed='false']").on("click", function() {

    $("#button_update").hide();
    $("#button_install").show();
    $("#button_reinstall").hide();
    $("#button_remove").hide();

});

$("#change_account").bind("click", function() {

    $('#modalAccounts').modal('show');

    return false;

});

$("#show_settings").on("click", function() {

    $('#modalSettings').modal('show');
    return false;

});
$("#show_servers").on("click", function() {

    $('#modalServers').modal('show');
    return false;

});
/*$(".btn-start-game").click( function() {

    $("#install-progress").modal('show');

});*/

$(".btn-add-fave").bind('click', function() {

    $(this).addClass( 'active' );

});

/*$(".search-form").keyup( function() {

    $('.servers').hide();
    $('.loader').fadeIn(600);

});*/















