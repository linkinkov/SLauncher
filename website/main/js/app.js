var setJavaPath = function (path) {

    $('#java_path').val(path);
}

var setMainPath = function (path) {

    $('#directory').val(path);
}
$(function () {

   
    var niceSelectStyle = {

        size: 5,
        container: 'body',

    };

    var niceSelect = function () {


        //  $("#developer_console").hide();
        $('#developer_console').selectpicker(niceSelectStyle);


        // $("#connection_quality").hide();
        $('#connection_quality').selectpicker(niceSelectStyle);


        // $("#action_on_launch").hide();
        $('#action_on_launch').selectpicker(niceSelectStyle);


    }


    $(document).bind('selectstart dragstart', function (e) {
        e.preventDefault();
        return false;
    });
    
    openAccount = function () {

        $('#modalAccounts').modal('show');
    }
    
    completeRefreshServers = function () {


        hideBlocker();

    };

    /*  $("#user_nick").keypress(function (e) {
     var strLenght = $("#user_nick").val().length;
     if (strLenght > 15) {

     e.preventDefault()

     }


     })*/


    var showBlocker = function () {

        $('.ui-blocker').toggle();

    }

    var hideBlocker = function () {


        $('.ui-blocker').toggle();
    }

    var onUpTopButton = function () {
        var val = $(this).attr("data-id")
var type="onUpTopbutton"
        LocalServerApi.setLink(type,val,{
            success: function (data) {
                console.log(data);
            },
            error: function (e) {
                alert(e);
            }
        })


       // app.TopButton(val);
    };

    var onSetColorButton = function () {
        var val = $(this).attr("data-id")
        var type="onSetColorButton"
        LocalServerApi.setLink(type,val,{
            success: function (data) {
                console.log(data);
            },
            error: function (e) {
                alert(e);
            }
        })
       // app.colorButton(val);
    };

    var onLikeButton = function () {
        var val = $(this).attr("data-id")
       var type='onLikeButton'
        LocalServerApi.setLink(type,val,{
            success: function (data) {
                console.log(data);
            },
            error: function (e) {
                alert(e);
            }
        })
       // app.likeButton(val);
    };

    var onBayDonatButton = function () {
        var val = $(this).attr("site")
        var type="BayDonat"
        LocalServerApi.setLink(type,val,{
            success: function (data) {
                console.log(data);
            },
            error: function (e) {
                alert(e);
            }
        })
       // app.BayDonat(val);
    };

    var openServerPage = function () {

        var server_id = $(this).attr("data-id");
        var type="OpenServerPage"
        LocalServerApi.setLink(type,server_id,{
            success: function (data) {
                console.log(data);
            },
            error: function (e) {
                alert(e);
            }
        })
       // app.OpenServerPage(server_id);
    }

    var onOpenFolder = function () {

       var  type="openFolder"
        var val=''
        LocalServerApi.setLink(type,val,{
            success: function (data) {
                console.log(data);
            },
            error: function (e) {
                alert(e);
            }
        })

      //  app.openFolder();
    };

    var onVkButton = function () {
        var type='onVkButton'
        var val=''
        LocalServerApi.setLink(type,val,{
            success: function (data) {
                console.log(data);
            },
            error: function (e) {
                alert(e);
            }
        })
        //app.vkButton();
    };
    
    var onaddServer = function () {
var type="addServer"
        var val=''
        LocalServerApi.setLink(type,val,{
            success: function (data) {
                console.log(data);
            },
            error: function (e) {
                alert(e);
            }
        })
      //  app.addServer();
    };
    
    var onTechSupport = function () {
            var type="TechSupport"
        var val=''
        LocalServerApi.setLink(type,val,{
            success: function (data) {
                console.log(data);
            },
            error: function (e) {
                alert(e);
            }
        })
       // app.TechSupport();
    };
    
    
    var AlertBadPassword = function () {

       var type="AlertBadPassword"
        var val=''
        LocalServerApi.setLink(type,val,{
            success: function (data) {
                console.log(data);
            },
            error: function (e) {
                alert(e);
            }
        })

       // app.AlertBadPassword();
    };

    var StartGame = function () {
        var version = $("#start_name_version").attr("name");

        console.log("VER:", version);
        if (version == undefined) {
            version = "1.10.2";

            //showBlocker();
            instalVersionGameLocal(version);

        } else {


            var type="StartGameLocal"
           // var val=''
            LocalServerApi.setLink(type,version,{
                success: function (data) {
                    console.log(data);
                },
                error: function (e) {
                    alert(e);
                }
            })

            //app.StartGameLocal(version);

        }


    };

    var cancelDownload = function () {
        console.log('отмена загрузки')
        var type="cancelDownload"
        var val=''
        LocalServerApi.setLink(type,val,{
            success: function (data) {
                console.log(data);
            },
            error: function (e) {
                alert(e);
            }
        })


        //app.cancelDownload();
        // hideBlocker();
    }

    var toMaincraftSite = function () {
        var type="toMaincraftSite"
        var val=''
        LocalServerApi.setLink(type,val,{
            success: function (data) {
                console.log(data);
            },
            error: function (e) {
                alert(e);
            }
        })
       // app.toMaincraftSite();
    };

    var MaincraftSite = function () {

        app.MaincraftSite();
    };

    var onChangeDirectory = function () {
        var val = $("#directory").val();

        var type="onChangeDirectory"

        var val=''
        LocalServerApi.setLink(type,val,{
            success: function (data) {
                console.log(data);
            },
            error: function (e) {
                alert(e);
            }
        })

       // app.onChangeDirectory(val)
    }

    var onChangeJavaPath = function () {
        var val = $("#java_path").val();
        var type="onChangeJavaPath"
        LocalServerApi.setLink(type,val,{
            success: function (data) {
                console.log(data);
            },
            error: function (e) {
                alert(e);
            }
        })



       // app.onChangeJavaPath(val)
    }
    var checkInstallGameStatus = function () {

        console.log("checkInstallGameStatus")

        LocalServerApi.InstallVersionGameStatus({

            success: function (data) {

                console.log("InstallVersionGameStatus success", data)

                var completed = data.response.completed;
                var started = data.response.started;
                var error = data.response.error;
                var progress = data.response.progress;
                var files = data.response.files;

                if (completed) {


                    setTimeout(function () {


                        callversion();
                        //$("#install-progress").modal('hide');
                        //  hideBlocker();

                    }, 100);


                } else if (error) {

                    //Добавить диалог с ошибкой
                    //$("#install-progress").modal('hide');

                    // hideBlocker();
                } else {

                    var item = renderLoader(data);

                    $(item).appendTo($('.modal-loader'));


                    // $('.progress-bar-striped').css('width',progress+'%');

                    //Идет процесс загрузки, ставим таймаут на проверку позже
                    setTimeout(checkInstallGameStatus, 100);

                }

            },
            error: function (msg) {

                //Ошибка сервака похоже
                //Нужно сделать диалог на такие ошибки
                console.log("InstallVersionGameStatus error", msg)
            }

        });

    }

    function renderLoader(data) {
        $('.modal-loader').html("")


        var title = ""
        var template = $("#loader_bar").html();
        var files = data.response.files;
        var end = files % 10;


        var ending = "";
        if (end > 1 && end <= 4) {
            ending = "а";
            console.log(ending);


        }
        if (end > 4 && end < 10 || end == 0) {
            ending = "ов";
            console.log(ending);
        }

        if (data.response.progress == "0") {
            title = true
        } else {
            title = false
        }

        Mustache.parse(template);
        var rendered = Mustache.render(template,
            {
                title: title,
                progress: data.response.progress,
                files: data.response.files,
                ending: ending
            });
        return rendered;

    }

    var refreshServers = function () {

        showBlocker();

    }

    $('#open_folder').bind('click', onOpenFolder);
    $('#vkButton').bind('click', onVkButton);
    $('#tech_support').bind('click', onTechSupport);
    $(".start-game").bind("click", StartGame);
    $("#refresh_servers").bind("click", refreshServers);
    $("#account_mojang a").bind("click", toMaincraftSite);
    $("#maincraft_site").bind("click", MaincraftSite);
    $("#account_no a").bind("click", toMaincraftSite);
    $("#maincraft_vk").bind("click", onVkButton);
    $('.btn-cancel-download').bind('click', cancelDownload);
    $('#change_account').bind('click', function () {

        $('#tab-accaunts a').tab('show')
        $('#tab a').tab('hide')
    })

    //  $(".change-directory").bind("click",onChangeDirectory);

    $("#button_install").bind("click", function () {

        console.log("button_install");
        //  showBlocker();
        $("#modalVersions").modal('hide');

        var version;

        for (var i = 0; i < $(".lbjs-list div").length; i++) {
            if ($($(".lbjs-list div")[i]).attr('selected')) {
                version = ($($(".lbjs-list div")[i]).attr('value'))

            }
        }


        instalVersionGameLocal(version)

    });

    function instalVersionGameLocal(version) {

        LocalServerApi.InstallVersionGame(version, 'local', {
            success: function (data) {

                console.log("InstallVersionGame:", data);
                if (!data.response.installed) {
                    //$("#install-progress").modal('show');
                    setTimeout(checkInstallGameStatus, 500);

                } else {

                    console.log("InstallVersionGame:", data, false);
                    callversion();
                    // hideBlocker();
                    //$("#install-progress").modal('hide');
                }


            },
            error: function (e) {
                alert(e)
                // hideBlocker();
            }
        })

    }
	
    var reinstal = function (version) {

        // showBlocker();
        LocalServerApi.reInstallVersionGame(version, {
            success: function (data) {
                //$("#install-progress").modal('show');
                setTimeout(checkInstallGameStatus, 500);


            },
            error: function (e) {
                //hideBlocker();
                //$("#install-progress").modal('hide');
            }
        })

    }

    $("#button_reinstall").bind("click", function () {
        var version;

        for (var i = 0; i < $(".lbjs-list div").length; i++) {
            if ($($(".lbjs-list div")[i]).attr('selected')) {
                version = ($($(".lbjs-list div")[i]).attr('value'))

            }
        }
        reinstal(version)
    });

    $("#button_remove").bind("click", function () {
        var version;

        for (var i = 0; i < $(".lbjs-list div").length; i++) {
            if ($($(".lbjs-list div")[i]).attr('selected')) {
                version = ($($(".lbjs-list div")[i]).attr('value'))

            }
        }
        LocalServerApi.RemoveVersionGame(version, {
            success: function (data) {


            },
            error: function (e) {
                alert(e)
            }
        })
        callversion();


    });

    $('.btn-edit-account').click(function () {
        var acc = $(".accounts").selectpicker('val');
        $('#user_nick').val(acc);
        $('#tab a').tab('show')
        LocalServerApi.getAccountType(acc, {
            success: function (data) {
                console.log(data.response.account_type)
                if (data.response.account_type == "ely") {
                    $("input[id='password_ely']").prop('checked', true)
                    $("#user_pass_ely").removeClass("hidden");
                    $("#user_pass_mojang").addClass("hidden");
                    $("#pay-acc").show()
                    $("#no-pass").hide()

                    $("#account_no").hide();
                    $("#account_mojang").hide();
                    $("#account_ely").show();
                }
                if (data.response.account_type == "mojang") {
                    $("input[id='password_mojang']").prop('checked', true)
                    $("#user_pass_mojang").removeClass("hidden");
                    $("#user_pass_ely").addClass("hidden");
                    $("#pay-acc").show()
                    $("#no-pass").hide()

                    $("#account_no").hide();
                    $("#account_mojang").show();
                    $("#account_ely").hide();

                }
                if (data.response.account_type == "free") {
                    $("input[id='password_no']").prop('checked', true)
                    $("#user_pass_mojang").addClass("hidden");
                    $("#user_pass_ely").addClass("hidden");
                    $("#pay-acc").hide()
                    $("#no-pass").show()

                    $("#account_no").show();
                    $("#account_mojang").hide();
                    $("#account_ely").hide();


                }
            },
            error: function (e) {

            }
        })

        $('.btn-create-account').hide();
        $('.btn-save-account').show();
    });

    $("#tab").click(function () {
        $('.btn-save-account').hide();
        $('.btn-create-account').show();
        $("input[id='password_no']").prop('checked', true)
        $("#user_pass_mojang").addClass("hidden");
        $("#user_pass_ely").addClass("hidden");
        $("#pay-acc").hide()
        $("#no-pass").show()

        $("#account_no").show();
        $("#account_mojang").hide();
        $("#account_ely").hide();


    })

    $(".about").click(function () {
        $(".btn-save-settings").hide()
        $(".set-default-setting").hide()

    })

    $(".start-minecrafr").click(function () {
        $(".btn-save-settings").show()
        $(".set-default-setting").show()

    })

    $(".mrlauncher-settings").click(function () {
        $(".btn-save-settings").show()
        $(".set-default-setting").show()

    })

    CreateAccount = function () {
        if (!($('#user_nick').val() == "")) {


            var password = "";
            var account_type = "";
            var user_nick = $("#user_nick").val();
            var acc_control = "create";

            if ($("#password_mojang").is(":checked")) {
                account_type = 'mojang';
                password = $('#user_pass_mojang').val();

            }

            if ($("#password_ely").is(":checked")) {
                account_type = 'ely';
                password = $('#user_pass_ely').val();

            }


            LocalServerApi.AccountControl(acc_control, user_nick, account_type, password, 'local', 'selected', {

                success: function (data) {
                    console.log(data.response.authentication)
                    if (!data.response.authentication && data.response.account_type != "") {
                        showBlocker();


                        setTimeout(checkAccStatus, 100);

                    } else {
                        console.log("Создан free аккаунт")
                        $('#modalAccounts').modal('hide');

                        for (var i = 0; i < $(".one-acc").length; i++) {
                            $(".one-acc").remove();
                        }
                        getUserslocal();
                    }
                },
                error: function (e) {

                    alert(e);
                }
            })

        }
    };

    $('.btn-save-account').bind('click', CreateAccount);

    $('.btn-create-account').bind('click', CreateAccount);

    $('.btn-del-account').bind("click", function () {
        var acc_control = "delete";
        var user_nick = $('.accounts').selectpicker('val');

        console.log("click del acc");

        LocalServerApi.AccountControl(acc_control, user_nick, '', '', 'local', '', {
            success: function (data) {

                console.log("load complete AccountControl");

                //$('#modalAccounts').modal('hide');

                for (var i = 0; i < $(".one-acc").length; i++) {
                    $(".one-acc").remove();
                }

                // setTimeout(function(){

                getUserslocal();
                // }, 1000)

            },
            error: function (e) {

            }
        });


    });

    $('.btn-set-account').bind('click', function () {
        var account = $(".accounts").selectpicker('val');

        LocalServerApi.SetAccount(account, "local", "active", {
            success: function (data) {
                var item = renderButtonUser(data['response'])
                $('.current-user-name').html(item);

                for (var i = 0; i < $('.user-acc li').length; i++) {

                    $($('.user-acc li')[i]).removeClass('active');

                    if ($($('.user-acc li')[i]).attr('name') == data['response']['username']) {

                        $($('.user-acc li')[i]).addClass('active');
                    }


                }


            },
            error: function (e) {
                alert(e)
            }
        })
        $('#modalAccounts').modal('hide');
    })
    
    //Users in heder
    function renderUsers(user) {
        var img = ""
        var template = $("#changedUser").html();

        if (user.type == 'ely') {
            img = 'images/favicon.jpg'
        }
        if (user.type == 'mojang') {
            img = 'images/favicon_majong_converted.png'
        }
        Mustache.parse(template);
        var rendered = Mustache.render(template,
            {
                username: user["username"],
                selected: user["selected"],
                img: img

            });
        return rendered;


    }

    // users in modal
    function renderUsers2(user) {
        //$(".accounts").html('');

        var template = $("#userTemplate").html();
        var img = ""
        Mustache.parse(template);
        if (user["selected"] == "active") {
            var selected = "selected"
        }
        if (user.type == 'ely') {
            img = 'images/favicon.jpg'
        }
        if (user.type == 'mojang') {
            img = 'images/favicon_majong_converted.png'
        }

        var rendered = Mustache.render(template,
            {
                username: user["username"],
                selected: selected,
                img: img

            });

        return rendered;
    }

    function showUsers(accounts) {
        $(".accountsCont").html('<select id="accounts-select" name="accounts" class="form-control accounts wide"></select>');
        for (var account_index in accounts) {
            var account = accounts[account_index];

            for (var acc_index in account) {

                var acc = account[acc_index];

                for (var obj_index in acc) {
                    var obj_acc = acc[obj_index];
                    var item = renderUsers(obj_acc);
                    var item2 = renderUsers2(obj_acc);


                    $(item).insertBefore($('.change-account'));

                    // $('.accounts').html(item2)
                    $(item2).appendTo($('.accounts'));

                }
            }
        }

        // $("#accounts-select").selectpicker(niceSelectStyle);
        // $("#accounts-select").ddslick();
        $('.accounts').selectpicker(niceSelectStyle);//.niceSelect();
        $("div.accounts").css("margin-bottom", 15);
        $("div.dropdown-menu.open").css('padding', 0);

        $('.one-acc').bind('click', function () {
            var acc = $(this).attr('name');
            var type = $(this).attr('data-type')

            $('.user-acc li').removeClass('active');

            $(this).closest('li').addClass('active');


            LocalServerApi.SetAccount(acc, "local", "active", {
                success: function (data) {


                    var item = renderButtonUser(data['response'])

                    $('.current-user-name').html(item);

                    for (var i = 0; i < $('.accounts option').length; i++) {


                        if ($($('.accounts option')[i]).attr("value") == data['response']['username']) {
                            $($('.accounts option')[i]).prop("selected", true);
                        } else {
                            $($('.accounts option')[i]).prop("selected", null);
                        }
                    }

                    $('.accounts').selectpicker('refresh');

                },
                error: function (e) {
                    alert(e)
                }
            })

        })

    }

    function renderButtonUser(user) {

        var template = $("#user-button").html();
        var img = ""
        if (user.type == 'premium' || user.type == 'mojang') {
            img = 'images/favicon_majong_converted.png'
        }
        Mustache.parse(template);

        var rendered = Mustache.render(template,
            {
                username: user.username,
                img: img
            });


        return rendered;
    }

    $('.set-default-setting').bind('click', function () {


        LocalServerApi.setDefaultSettings({
            success: function (data) {

                $('#launch').html("");
                $('#settings').html("");

                for (var setting_id in data) {
                    var seting = data[setting_id];


                    var item = renderSettingsMain(seting);
                    var item2 = renderSettingsLaunch(seting);
                    $(item).appendTo($('#launch'));
                    $(item2).appendTo($('#settings'));

                    atributeSet(seting)
                    niceSelect();


                }


                $("div.dropdown-menu.open").css('padding', 0);
                $('#java_path').val('');
            },
            error: function (e) {
                alert(e)
            }
        })
        $(".change-directory").bind("click", onChangeDirectory);
    })

    $('.btn-save-settings').bind('click', function () {

        var directory = $("#directory").val();
        var weight = $("#weight").val();
        var height = $("#height").val();
        var fullscreen = $("#fullscreen").is(":checked");
        var show_snapshots = $("#show_snapshots").is(":checked");
        var show_beta = $("#show_beta").is(":checked");
        var show_alpha = $("#show_alpha").is(":checked");
        var show_old_version = $("#show_old_version").is(":checked");

        var show_mod_version = $("#show_mod_version").is(":checked");
        var jvm_arg = $("#jvm_arg").val();
        var minecraft_arg = $("#minecraft_arg").val();
        var java_path = $("#java_path").val();
        var memory_alloc = $("#amount").val();
        var autostart = $("#auto_start").is(":checked");
        var developer_console = $("#developer_console").selectpicker('val')
        var connection_quality = $("#connection_quality").selectpicker('val')
        var action_on_launch = $("#action_on_launch").selectpicker('val')
        var language = $("#language").val();
        var full_comand = $("#full_comand").is(":checked");


        LocalServerApi.saveSetings(
            directory,
            weight,
            height,
            fullscreen,
            show_snapshots,
            show_beta,
            show_alpha,
            show_old_version,
            show_mod_version,
            jvm_arg,
            minecraft_arg,
            java_path,
            memory_alloc,
            autostart,
            developer_console,
            connection_quality,
            action_on_launch,
            full_comand,
            language, {

                success: function (data) {


                    $('#launch').html("");
                    $('#settings').html("");

                    for (var setting_id in data) {
                        var seting = data[setting_id];


                        var item = renderSettingsMain(seting);
                        var item2 = renderSettingsLaunch(seting)

                        $(item).appendTo($('#launch'));
                        $(item2).appendTo($('#settings'));
                        // $(item).appendTo($('.content'));
                        atributeSet(seting);

                        niceSelect()

                    }
                    $("div.dropdown-menu.open").css('padding', 0);
                    callversion();

                },
                error: function (e) {

                    alert(e);
                }
            })
        $(".change-directory").bind("click", onChangeDirectory);
        /*  $('.my-btn').css({"border-radius":10+'px'
         ,"border":1+'px',
         "border-color":"#000000"})*/

    });

    function atributeSet(seting) {


        for (var i = 0; i < $('.form-group input[data-check] ').length; i++) {
            // console.log($($('.form-group input[data-check] ')[i].)

            $($('.form-group input[data-check] ')[i]).removeAttr("checked")
            if ($($('.form-group input[data-check] ')[i]).attr("data-check") == "true") {
                $($('.form-group input[data-check] ')[i]).attr("checked", "checked")

            }
            for (var j = 0; j < $('#settings option').length; j++) {
                $($('#settings option')[j]).prop("selected", null)
                if ($($('#settings option')[j]).attr('value') == seting["developer_console"]) {

                    $($('#settings option')[j]).prop("selected", "selected")

                }
                if ($($('#settings option')[j]).attr('value') == seting["connection_quality"]) {
                    $($('#settings option')[j]).prop("selected", "selected")
                }
                if ($($('#settings option')[j]).attr('value') == seting["action_on_launch"]) {
                    $($('#settings option')[j]).prop("selected", "selected")
                }
                /*  if ($($('#settings option')[j]).attr('value') == seting["language"]) {
                 $($('#settings option')[j]).attr("selected", "selected")

                 }*/
                for (var k = 0; k < $(".arguments input").length; k++) {
                    if ($($(".arguments input")[k]).attr("value") == "null") {
                        $($(".arguments input")[k]).removeAttr("value")
                    }
                }

            }

        }

        $('.bootstrap-select').selectpicker('refresh');
        memSlider(seting['memory_alloc'])

    }

    function renderSettingsMain(setting) {
        var template = $('#user_settings_maincraft').html();
        Mustache.parse(template);   // optional, speeds up future uses

        java_path = '';
        if (setting["java_path"])
            java_path = setting["java_path"];
        var rendered = Mustache.render(template,
            {


                directory: setting["directory"],
                weight: setting["weight"],
                height: setting["height"],
                fullscreen: setting["fullscreen"],
                show_snapshots: setting["show_snapshots"],
                show_beta: setting["show_beta"],
                show_alpha: setting["show_alpha"],
                show_old_version: setting["show_old_version"],
                show_mod_version: setting["show_mod_version"],
                jvm_arg: setting["jvm_arg"],
                minecraft_arg: setting["minecraft_arg"],
                java_path: java_path,

                memory_alloc: setting["memory_alloc"],


            });


        return rendered;


    }

    function renderSettingsLaunch(setting) {

        var template = $('#user_settings_launch').html();
        Mustache.parse(template);   // optional, speeds up future uses
        var rendered = Mustache.render(template,
            {
                developer_console: setting["developer_console"],
                connection_quality: setting["connection_quality"],
                action_on_launch: setting["action_on_launch"],
                full_comand: setting["full_comand"],

                language: setting["language"],
                auto_start: setting["auto_start"],

            });


        return rendered;

    }

    onClickStartGame = function () {
        var data_ip = $(this).attr("ip");
        var data_server_name = $(this).attr("server_name")
        var data_version = $(this).attr("version")


        if (data_ip.indexOf(":") < 0) {

            data_ip = data_ip + ":25565";
        }

        if (data_version.indexOf(",") >= 0) {

            data_version = data_version.split(",")[0] + "";

            data_version = data_version.substr(0, data_version.length - 2);

        }
        // $("#install-progress").modal('show');
        // showBlocker();

        LocalServerApi.StartGame(data_ip, data_server_name, data_version, "local", {

            success: function (data) {
                if (data.response.hasOwnProperty('game_started') && data.response.game_started) {
                    $("#install-progress").modal('hide');
                    // hideBlocker()
                } else {
                    if (!data.response.installed) {
                        //$("#install-progress").modal('show');
                        $('.btn-cancel-download').attr('data', data_version)

                        setTimeout(checkInstallGameStatus, 500);
                    } else {
                        //$("#install-progress").modal('hide');
                        callversion();
                        //hideBlocker();

                    }
                }

            },
            error: function (e) {

                // hideBlocker();

            }
        })


    };

    function onClickVersion() {
        currentPage = 0;

        currentVersion = $(this).data('url');

        selectCurrentVersion();
        serversUpdate();
    }

    function onClickParam() {
        flag = false;
        $('.btn-like-servers').removeClass("active")
        currentPage = 0;
        currentParam = $(this).data('url');

        selectCurrentParam();
        serversUpdate();
    }

    function memSlider(val) {
        $("#slider").slider({

            step: 256,
            min: 512,
            max: 3072,
            values: [val],

            slide: function (event, ui) {
                $("#amount").val(ui.values[0]);
            }
        }).slider('pips', {
            //first: 512,
            //last: 3072,

            // labels: [512,768,1024,1280,1536,1792,2048,2304,2560,2816,3072],
            rest: "label",
            step: 2
            // prefix: "",
            //suffix: ""
        });
        $("#amount").val($("#slider").slider("values", 0));
    }


    // $(document).ready(function () {


    //var target = $('.servers')[0];
    //server_spinner = new Spinner(opts).spin(target);

    flag = false;
    servers = null;
    versions = null;
    main_params = null;
    mini_game_params = null;
    completeCount = 0;

    currentVersion = "all";
    currentParam = "all";
    currentPage = 0;
    countParams = 0;

    countOnPage = 25;
    number_consecut = 1;
    searchQuery = "";
    my_servers = {};

    function renderVersion(version) {
        var template = $('#versionTemplate').html();
        Mustache.parse(template);   // optional, speeds up future uses
        var rendered = Mustache.render(template,
            {url: version["url"]}
        );

        return rendered;
    }

    function renderParam(param, type) {


        var template = $('#paramTemplate').html();
        Mustache.parse(template);   // optional, speeds up future uses
        var rendered = Mustache.render(template,
            {
                url: param["url"],
                title: param["title"],
                type: type
            }
        );

        return rendered;
    }

    function showAllVersions(versions) {

        $('.version-item').remove();

        for (var version_index in versions.version) {
            var version = versions.version[version_index];

            var item = renderVersion(version);

            $(item).appendTo($('.versions ul'));
        }

        selectCurrentVersion();

        $('.versions li a').bind('click', onClickVersion);

    }

    function selectCurrentVersion() {

        $('.versions li').each(function (index) {
            if ($(this).data('url') == currentVersion) {

                $(this).addClass('active');
            } else {
                $(this).removeClass('active');
            }
        })
    }

    function selectCurrentParam() {

        $('.search li').each(function (index) {
            if ($(this).data('url') == currentParam) {

                $(this).addClass('active');
            } else {
                $(this).removeClass('active');
            }
        })
    }

    function checkCompleteFullLoading() {


        if (completeCount == 4 && servers && versions && main_params && mini_game_params) {


            //успешно все загружено
            showAllVersions(versions);

            showAllMainParams(main_params);

            showAllMiniGameParams(mini_game_params);

            showServers(servers);


            selectCurrentParam();

            $('.search li a').bind('click', onClickParam);

            hideBlocker();


        } else if (completeCount == 4) {

            //что-то не загрузилось
            alert("Не удалось загрузить данные" + servers + versions + main_params + mini_game_params);

        } else {

            //продолжается процесс загрузки
        }

    }

    function paramMainUpdate(callbacks) {

        RemoteServerApi.getParams('main', {
            success: function (data) {

                main_params = data;

                showAllMainParams(main_params);

                if (callbacks && callbacks.success) {

                    callbacks.success();
                }


            },
            error: function (e) {

                if (callbacks && callbacks.error) {

                    callbacks.error();
                }

            }
        });
    }

    function versionsUpdate(callbacks) {

        RemoteServerApi.getVersions({
            success: function (data) {
                //console.log(data);
                versions = data;

                showAllVersions(versions);

                if (callbacks && callbacks.success) {

                    callbacks.success();
                }


            },
            error: function (e) {

                if (callbacks && callbacks.error) {

                    callbacks.error();
                }

            }
        });
    }


    function renderLocalVersioninstallButton(version) {

        var template = $('#local-version-button').html();

        Mustache.parse(template);   // optional, speeds up future uses

        var rendered = Mustache.render(template,
            {
                id: version['id'],
                installed: version['installed'],
                selected: version["selected"],
                name: regularExpression(version)


            }
        );

        return rendered;

    }

    function renderLocalVersioninstall(version) {


        var template = $('#local-version').html();


        Mustache.parse(template);   // optional, speeds up future uses
        if (version['selected'] == "active") {
            var selected = 'selected';
        }

        var rendered = Mustache.render(template,
            {
                id: version['id'],
                installed: version['installed'],
                selected: selected,
                name: regularExpression(version)

            }
        );

        return rendered;

    }

    function renderVersionSelect() {
        var template = $('#version-select').html();
        $(".version-select").html('')
        $(template).appendTo(".version-select")

    }

    function regularExpression(version) {
        if (version["id"].substring(0, 2) == "1.") {
            var name = "Версия " + version["id"]
        }
        if (version["id"].substring(0, 1) == "b") {
            name = "Бетта " + version["id"].substr(1)
        }
        if (version["id"].substring(0, 1) == "a" || version["id"].substring(0, 1) == "c") {
            name = "Альфа " + version["id"].substr(1)
        }
        if (version["id"].substring(2, 3) == "w") {
            name = "Снапшот " + version["id"]
        }
        if (version["id"].match("[Pp]re")) {

            name = "Снапшот " + version["id"]
        }

        return name;
    }

    function showlocalVersion(data) {

        for (var version_id in data) {

            var version = data[version_id];
            for (var version_i in version) {

                var vers = version[version_i];

                var item = renderLocalVersioninstall(vers);
                var ittem = renderLocalVersioninstallButton(vers);
                if ((vers["id"] != 'latest-snapshot') && ( vers["id"] != 'latest-release')) {
                    if (vers["installed"] == "true") {

                        $(ittem).insertBefore($('#manage_versions').closest('li'));
                        $(item).insertBefore($('#not-install'));

                    } else {
                        $(item).appendTo($('select#versions'));

                    }
                }
            }
        }
        // $('#versions').selectpicker(niceSelectStyle);

        /* for(var i=0;i<$('#versions option').length;i++){
         $($('#verions option')[i]).css('padding',10);
         $($('#versions option')[i]).css('margin-top',20);
         console.log($($('#versions option')[i]).val())
         }*/

        $('#versions').listbox({'searchbar': false})
        $('.on-version-button').bind('click', function () {
            var set_version = $(this).attr('name');

            $(".version-game li").removeClass("active");

            $(this).closest('li').addClass('active');
            LocalServerApi.setLocalVersion(set_version, {
                success: function (data) {

                    if (data.hasOwnProperty("response") && data.response.status) {


                        var item = renderButtonVersion(data.response)
                        //  console.log(data.response)

                        $('.current-version-name').html(item);
                        for (var i = 0; i < $(".version-game li").length; i++) {

                            $($(".version-game li")[i]).removeClass("active");

                            if ($($(".version-game li a")[i]).attr("name") == data.response.id) {
                                $($('.version-game li')[i]).addClass("active");


                            }
                        }


                    } else {


                        //Ошибка данных
                    }


                },
                error: function (msg) {

                }
            })
            $(this).closest('li').addClass('active');

        })


        $(".lbjs-list div[data-installed='false']").on("click", function () {

            $("#button_update").hide();
            $("#button_install").show();
            $("#button_reinstall").hide();
            $("#button_remove").hide();
        });

        $(".lbjs-list div[data-installed='true']").on("click", function () {

            if ($(this).attr("data-new-update") == 'true') {
                $("#button_update").show();
            } else {
                $("#button_update").hide();
            }

            $("#button_install").hide();
            $("#button_reinstall").show();
            $("#button_remove").show();

        });

        $(".lbjs-list div[data-installed='true']").first().click();
        $(".lbjs-list div[data-installed='true']").on("click", function () {

            if ($(this).attr("data-new-update") == 'true') {
                $("#button_update").show();
            } else {
                $("#button_update").hide();
            }

            $("#button_install").hide();
            $("#button_reinstall").show();
            $("#button_remove").show();


        });
    }
	$('.btn-start-game').bind('click', onClickStartGame);

    function renderButtonVersion(version) {

        var template = $("#version-button").html();
        Mustache.parse(template);

        var rendered = Mustache.render(template,
            {
                version: version['id'],
                selected: version["selected"],
                name: regularExpression(version)
            });

        return rendered;
    }

    function removeversionsButton() {
        $(".versions-button").remove();
    }

    $(document).ready(function () {




        // var handshakeOk=function(port) {
        callversion = function () {
            LocalServerApi.getLocalVersion({
                success: function (data) {



                    //$('.servers1').html("Test:"+ JSON.stringify(data));
                    removeversionsButton()
                    renderVersionSelect()
                    showlocalVersion(data)
                    if (data.hasOwnProperty("response")) {

                        var objects = data.response
                        for (var obj in objects) {
                            var version = objects[obj];
                            var sch = 0;
                            var objects = data.response;
                            for (var obj in objects) {
                                var version = objects[obj];
                                if (version.installed == "true") {
                                    sch = +1;

                                }
                            }
                            if (sch > 0) {
                                for (var obj in objects) {
                                    var version = objects[obj];
                                    if (version.selected == 'active') {

                                        var item = renderButtonVersion(version)
                                        $('.current-version-name').html(item);

                                    }
                                }
                            }
                            else {
                                $('.current-version-name').html("установите версию");
                            }


                        }
                    }


                },
                error: function (e) {
                    // $('.servers1').html("Error:"+data.length);


                }

            })

        }

        oldVersionCount = 0;

        var getLocalVersionCallbacks = {

            success: function (data) {



                //$('.servers1').html("Test:"+ JSON.stringify(data));

                if (data.response.length <= 0) {


                    setTimeout(function () {
                        LocalServerApi.getLocalVersion(getLocalVersionCallbacks);


                    }, 5000)


                } else {
                    removeversionsButton()
                    renderVersionSelect()
                    showlocalVersion(data)
                    if (data.hasOwnProperty("response")) {
                        var sch = 0;
                        var objects = data.response;
                        for (var obj in objects) {
                            var version = objects[obj];
                            if (version.installed == "true") {
                                sch = +1;

                            }
                        }
                        if (sch > 0) {
                            for (var obj in objects) {
                                var version = objects[obj];
                                if (version.selected == 'active') {

                                    var item = renderButtonVersion(version)
                                    $('.current-version-name').html(item);

                                }
                            }
                        }
                        else {
                            $('.current-version-name').html("установите версию");
                        }
                    }
                }


            },
            error: function (e) {
                // $('.servers1').html("Error:"+data.length);


                setTimeout(function () {
                    LocalServerApi.getLocalVersion(getLocalVersionCallbacks);
                }, 3000)
            }
        }

        setTimeout(function () {

            LocalServerApi.getLocalVersion(getLocalVersionCallbacks);

        }, 5000);


        LocalServerApi.getSettings({
            success: function (data) {

                $('#launch').html("");
                $('#settings').html("");

                for (var setting_id in data) {
                    var seting = data[setting_id];


                    var item = renderSettingsMain(seting);
                    var item2 = renderSettingsLaunch(seting)

                    //$(item).insertBefore($('.content'));
                    //$('.content').html(item)
                    $(item).appendTo($('#launch'));
                    $(item2).appendTo($('#settings'));
                    atributeSet(seting);


                }

                $(".change-directory").bind("click", onChangeDirectory);
                $(".change-java-path").bind("click", onChangeJavaPath);

                niceSelect();
                $("div.dropdown-menu.open").css('padding', 0);


            },


            error: function (e) {
                alert(e);

            }


        })

        getUserslocal = function () {



            LocalServerApi.getUsers({
                success: function (data) {
                    users = data;


                    showUsers(users)
                    for (var user_id in data) {
                        var user_i = data[user_id];
                        for (var user_ in user_i) {
                            var user = user_i[user_];
                            for (var use in user) {
                                var us = user[use];
                                if (us["selected"] == 'active') {


                                    var item = renderButtonUser(us)
                                    $('.current-user-name').html(item);

                                }

                            }

                        }

                    }
                },
                error: function (e) {
                    alert(e);

                }
            });
        }

        getUserslocal();
        // }

        //var handshakeError=function(msg){

        //  $('.servers').html("Ошибка подключения к MRLauncher:"+msg);

        //}


        /* LocalServerApi.handshake("local",{

         success:handshakeOk,
         error:handshakeError
         });*/

        RemoteServerApi.getVersions({
            success: function (data) {
                //console.log(data);
                versions = data;
                completeCount++;
                checkCompleteFullLoading();
            },
            error: function (e) {

                alert(e);
                completeCount++;
                checkCompleteFullLoading();
            }
        });

        RemoteServerApi.getParams("main", {
            success: function (data) {


                main_params = data;
                completeCount++;
                checkCompleteFullLoading();
            },
            error: function (e) {

                alert(e);
                completeCount++;
                checkCompleteFullLoading();
            }
        });

        RemoteServerApi.getParams("mini_games", {
            success: function (data) {

                mini_game_params = data;
                completeCount++;
                checkCompleteFullLoading();
            },
            error: function (e) {
                completeCount++;
                checkCompleteFullLoading();
            }
        });

    })


})