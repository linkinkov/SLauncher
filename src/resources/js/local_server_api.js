LocalServerApiClassPort = 8000;
function LocalServerApiClass() {






    /*this.handshake=function(context,callbacks ){


     tryCount=0;
     maxTryCount=3;
     start_port=8000;
     end_port=8200;

     LocalServerApiClassPort=start_port;


     var findMRLauncher=function(incontext, port, incallback ) {
     var url = "http://localhost:" + port + "/handshake?jsoncollback=?";
     $.ajax({
     url: url,
     data: {
     mrl_site_api: "1.0.0",
     context: incontext
     },
     dataType: 'jsonp',
     cache: false,
     success: function (data) {

     if (incallback && incallback.hasOwnProperty('success')) {
     incallback.success($.parseJSON(data));
     }
     },
     error: function (xhr, ajaxOptions, thrownError) {
     if (incallback && incallback.hasOwnProperty('error')) {
     incallback.error(thrownError);
     }
     }
     });
     }

     var forPortCallbacks={

     success: function (data) {


     if(data.response && data.response.status && data.response.hasOwnProperty("mrl_launcher")) {
     if (callbacks && callbacks.hasOwnProperty('success')) {
     callbacks.success(LocalServerApiClassPort);
     }
     }else{

     if(LocalServerApiClassPort>end_port){

     tryCount++;
     LocalServerApiClassPort=start_port;

     if(tryCount>maxTryCount){

     if (callbacks && callbacks.hasOwnProperty('error')) {
     callbacks.error(msg);
     }


     return;
     }
     }


     findMRLauncher(context, LocalServerApiClassPort, forPortCallbacks);
     }

     },
     error:function(msg){


     LocalServerApiClassPort++;
     if(LocalServerApiClassPort>end_port){

     tryCount++;
     LocalServerApiClassPort=start_port;

     if(tryCount>maxTryCount){

     if (callbacks && callbacks.hasOwnProperty('error')) {
     callbacks.error(msg);
     }


     return;
     }
     }


     findMRLauncher(context, LocalServerApiClassPort, forPortCallbacks);
     }
     };

     findMRLauncher(context, LocalServerApiClassPort, forPortCallbacks);


     }*/
    
    
    
    


    this.StartGame = function (data_ip, data_server_name, data_version, context, callbacks) {

        if (!LocalServerApiClassPort) {

            if (callbacks && callbacks.hasOwnProperty('error')) {
                callbacks.error("MRLauncher not running");
            }

            return;
        }

        var url = "http://localhost:" + LocalServerApiClassPort + "/start_game?jsoncollback=?";
        $.ajax({
            url: url,
            data: {
                ip: data_ip,
                name: data_server_name,
                version: data_version,
                context: context
            },
            dataType: 'jsonp',
            timeout:1000,
            cache: false,
            success: function (data) {

                if (callbacks && callbacks.hasOwnProperty('success')) {
                    callbacks.success($.parseJSON(data));
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {
                if (callbacks && callbacks.hasOwnProperty('error')) {
                    callbacks.error(thrownError);
                }
            }
        });
    }

   /* this.getStatusGame = function (callbacks) {
        var url = "http://localhost:" + LocalServerApiClassPort + "/status_game?jsoncollback=?"
        $.ajax({
            url: url,
            dataType: 'jsonp',
            cache: false,

            success: function (data) {

                if (callbacks && callbacks.hasOwnProperty('success')) {
                    callbacks.success($.parseJSON(data));
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {
                if (callbacks && callbacks.hasOwnProperty('error')) {
                    callbacks.error(thrownError);
                }
            }
        });
    }*/
    
    
    
    

    this.InstallVersionGame = function (version, context, callbacks) {

        if (!LocalServerApiClassPort) {

            if (callbacks && callbacks.hasOwnProperty('error')) {
                callbacks.error("MRLauncher not running");
            }

            return;
        }


        var url = "http://localhost:" + LocalServerApiClassPort + "/install_game?jsoncollback=?";
        $.ajax({
            url: url,
            data: {
                version: version,
            },
            dataType: 'jsonp',
            cache: false,
            success: function (data) {

                if (callbacks && callbacks.hasOwnProperty('success')) {
                    callbacks.success($.parseJSON(data));
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {
                if (callbacks && callbacks.hasOwnProperty('error')) {
                    callbacks.error(thrownError);
                }
            }
        });
    }
    this.getLikeServers = function (page,count_on_page,query,callbacks) {

        if (!LocalServerApiClassPort) {

            if (callbacks && callbacks.hasOwnProperty('error')) {
                callbacks.error("MRLauncher not running");
            }

            return;
        }


        var url = "http://localhost:" + LocalServerApiClassPort + "/getLikeServers?jsoncollback=?";
        $.ajax({
            url: url,
            data: {
                page:page,
                count_on_page:count_on_page,
                query:query
            },
            dataType: 'jsonp',
            cache: false,
            success: function (data) {

                if (callbacks && callbacks.hasOwnProperty('success')) {
                    callbacks.success($.parseJSON(data));
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {
                if (callbacks && callbacks.hasOwnProperty('error')) {
                    callbacks.error(thrownError);
                }
            }
        });
    }

    this.InstallVersionGameStatus = function ( callbacks) {

        if (!LocalServerApiClassPort) {

            if (callbacks && callbacks.hasOwnProperty('error')) {
                callbacks.error("MRLauncher not running");
            }

            return;
        }


        var url = "http://localhost:" + LocalServerApiClassPort + "/install_game_status?jsoncollback=?";
        $.ajax({
            url: url,
            data: {},
            dataType: 'jsonp',
            cache: false,
            success: function (data) {

                if (callbacks && callbacks.hasOwnProperty('success')) {
                    callbacks.success($.parseJSON(data));
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {
                if (callbacks && callbacks.hasOwnProperty('error')) {
                    callbacks.error(thrownError);
                }
            }
        });
    }

    this.authAccStatus = function ( callbacks) {

        if (!LocalServerApiClassPort) {

            if (callbacks && callbacks.hasOwnProperty('error')) {
                callbacks.error("MRLauncher not running");
            }

            return;
        }


        var url = "http://localhost:" + LocalServerApiClassPort + "/auth_acc_status?jsoncollback=?";
        $.ajax({
            url: url,
            data: {

            },
            dataType: 'jsonp',
            cache: false,
            success: function (data) {

                if (callbacks && callbacks.hasOwnProperty('success')) {
                    callbacks.success($.parseJSON(data));
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {
                if (callbacks && callbacks.hasOwnProperty('error')) {
                    callbacks.error(thrownError);
                }
            }
        });
    }
    
    
    
    this.getAccountType= function (acc,callbacks) {
        var url="http://localhost:" + LocalServerApiClassPort + "/get_acc_type?jsoncollback=?";
        $.ajax({
            url: url,
            data: {
                account:acc

            },
            dataType: 'jsonp',
            cache: false,
            success: function (data) {

                if (callbacks && callbacks.hasOwnProperty('success')) {
                    callbacks.success($.parseJSON(data));
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {
                if (callbacks && callbacks.hasOwnProperty('error')) {
                    callbacks.error(thrownError);
                }
            }
        });
    }

    this.reInstallVersionGame = function (version,  callbacks) {

        if (!LocalServerApiClassPort) {

            if (callbacks && callbacks.hasOwnProperty('error')) {
                callbacks.error("MRLauncher not running");
            }

            return;
        }


        var url = "http://localhost:" + LocalServerApiClassPort + "/reinstal_version_game?jsoncollback=?";
        $.ajax({
            url: url,
            data: {
                version: version
            },
            dataType: 'jsonp',
            cache: false,
            success: function (data) {

                if (callbacks && callbacks.hasOwnProperty('success')) {
                    callbacks.success($.parseJSON(data));
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {
                if (callbacks && callbacks.hasOwnProperty('error')) {
                    callbacks.error(thrownError);
                }
            }
        });
    }

    this.RemoveVersionGame = function (version, callbacks) {

        if (!LocalServerApiClassPort) {

            if (callbacks && callbacks.hasOwnProperty('error')) {
                callbacks.error("MRLauncher not running");
            }

            return;
        }


        var url = "http://localhost:" + LocalServerApiClassPort + "/remove_version_game?jsoncollback=?";
        $.ajax({
            url: url,
            data: {
                version: version
            },
            dataType: 'jsonp',
            cache: false,
            success: function (data) {

                if (callbacks && callbacks.hasOwnProperty('success')) {
                    callbacks.success($.parseJSON(data));
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {
                if (callbacks && callbacks.hasOwnProperty('error')) {
                    callbacks.error(thrownError);
                }
            }
        });
    }
    /* this.setVersionGame=function(version,callbacks ) {
     var url = "http://localhost:8000/set_version_game?jsoncollback=?";
     $.ajax({
     url: url,
     data: {
     version: version
     },
     dataType: 'jsonp',
     cache: false,
     success: function (data) {

     if (callbacks && callbacks.hasOwnProperty('success')) {
     callbacks.success($.parseJSON(data));
     }
     },
     error: function (xhr, ajaxOptions, thrownError) {
     if (callbacks && callbacks.hasOwnProperty('error')) {
     callbacks.error(thrownError);
     }
     }
     });
     }*/
    this.getLocalVersion = function (callbacks) {

        if (!LocalServerApiClassPort) {

            if (callbacks && callbacks.hasOwnProperty('error')) {
                callbacks.error("MRLauncher not running");
            }

            return;
        }

        var url = "http://localhost:" + LocalServerApiClassPort + "/get_local_version?jsoncollback=?"
        $.ajax({
            url: url,
            dataType: 'jsonp',
            cache: false,
            success: function (data) {

                if (callbacks && callbacks.hasOwnProperty('success')) {
                    callbacks.success($.parseJSON(data));
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {

                if (callbacks && callbacks.hasOwnProperty('error')) {
                    callbacks.error(thrownError);
                }
            }
        });

    }

    this.setLocalVersion = function (version, callbacks) {

        if (!LocalServerApiClassPort) {

            if (callbacks && callbacks.hasOwnProperty('error')) {
                callbacks.error("MRLauncher not running");
            }

            return;
        }

        var url = "http://localhost:" + LocalServerApiClassPort + "/set_version_game?jsoncollback=?"
        $.ajax({
            url: url,
            data: {
                version: version
            },
            dataType: 'jsonp',
            cache: false,
            success: function (data) {

                if (callbacks && callbacks.hasOwnProperty('success')) {
                    callbacks.success($.parseJSON(data));
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {

                if (callbacks && callbacks.hasOwnProperty('error')) {
                    callbacks.error(thrownError);
                }
            }
        });

    }

    this.AccountControl = function (acc_control, username, account_type, password, context, selected, callbacks) {


        if (!LocalServerApiClassPort) {

            if (callbacks && callbacks.hasOwnProperty('error')) {
                callbacks.error("MRLauncher not running");
            }

            return;
        }


        var url = "http://localhost:" + LocalServerApiClassPort + "/create_account?jsoncollback=?"
        $.ajax({
            url: url,
            data: {
                acc_contol: acc_control,
                username: username,
                account_type: account_type,
                password: password,
                context: context,
               
            },

            dataType: 'jsonp',
            cache: false,
            success: function (data) {

                if (callbacks && callbacks.hasOwnProperty('success')) {
                    callbacks.success($.parseJSON(data));
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {

                if (callbacks && callbacks.hasOwnProperty('error')) {
                    callbacks.error(thrownError);
                }
            }
        });

    }
    /* this.saveSetings=function(params, callbacks) {
     var url = "http://localhost:8000/save_settings?jsoncollback=?"

     $.ajax({
     url: url,
     data:{
     params: params
     },

     dataType: 'jsonp',
     cache: false,
     success: function (data) {

     if (callbacks && callbacks.hasOwnProperty('success')) {
     callbacks.success($.parseJSON(data));
     }
     },
     error: function (xhr, ajaxOptions, thrownError) {

     if (callbacks && callbacks.hasOwnProperty('error')) {
     callbacks.error(thrownError);
     }
     }
     });
     }*/
    this.setDefaultSettings = function (callbacks) {

        if (!LocalServerApiClassPort) {

            if (callbacks && callbacks.hasOwnProperty('error')) {
                callbacks.error("MRLauncher not running");
            }

            return;
        }


        var url = "http://localhost:" + LocalServerApiClassPort + "/set_default_settings?jsoncollback=?";
        $.ajax({
            url: url,
            dataType: 'jsonp',
            cache: false,
            success: function (data) {

                if (callbacks && callbacks.hasOwnProperty('success')) {
                    callbacks.success($.parseJSON(data));
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {

                if (callbacks && callbacks.hasOwnProperty('error')) {
                    callbacks.error(thrownError);
                }
            }
        });

    }

    this.getSettings = function (callbacks) {

        if (!LocalServerApiClassPort) {

            if (callbacks && callbacks.hasOwnProperty('error')) {
                callbacks.error("MRLauncher not running");
            }

            return;
        }


        var url = "http://localhost:" + LocalServerApiClassPort + "/get_setting?jsoncollback=?"

        $.ajax({
            url: url,
            dataType: 'jsonp',
            cache: false,
            success: function (data) {

                if (callbacks && callbacks.hasOwnProperty('success')) {
                    callbacks.success($.parseJSON(data));
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {

                if (callbacks && callbacks.hasOwnProperty('error')) {
                    callbacks.error(thrownError);
                }
            }
        });
    }

    this.saveSetings = function (directory,
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
                                 language, callbacks) {

        if (!LocalServerApiClassPort) {

            if (callbacks && callbacks.hasOwnProperty('error')) {
                callbacks.error("MRLauncher not running");
            }

            return;
        }


        var url = "http://localhost:" + LocalServerApiClassPort + "/save_setting?jsoncollback=?"

        $.ajax({
            url: url,
            data: {
                directory: directory,
                weight: weight,
                height: height,
                fullscreen: fullscreen,
                show_snapshots: show_snapshots,
                show_beta: show_beta,
                show_alpha: show_alpha,
                show_old_version: show_old_version,
                show_mod_version: show_mod_version,
                jvm_arg: jvm_arg,
                minecraft_arg: minecraft_arg,
                java_path: java_path,
                memory_alloc: memory_alloc,
                autostart:autostart,
                developer_console: developer_console,
                connection_quality: connection_quality,
                action_on_launch: action_on_launch,
                full_comand:full_comand,
                language: language
            },

            dataType: 'jsonp',
            cache: false,
            success: function (data) {

                if (callbacks && callbacks.hasOwnProperty('success')) {
                    callbacks.success($.parseJSON(data));
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {

                if (callbacks && callbacks.hasOwnProperty('error')) {
                    callbacks.error(thrownError);
                }
            }
        });
    }


    this.getUsers = function (callbacks) {

        if (!LocalServerApiClassPort) {

            if (callbacks && callbacks.hasOwnProperty('error')) {
                callbacks.error("MRLauncher not running");
            }

            return;
        }


        var url = "http://localhost:" + LocalServerApiClassPort + "/all_accounts?jsoncollback=?";
        $.ajax({
            url: url,
            dataType: 'jsonp',
            cache: false,
            success: function (data) {

                if (callbacks && callbacks.hasOwnProperty('success')) {
                    callbacks.success($.parseJSON(data));
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {

                if (callbacks && callbacks.hasOwnProperty('error')) {
                    callbacks.error(thrownError);
                }
            }
        });
    }

    this.SetAccount = function (account, context, selected, callbacks) {


        if (!LocalServerApiClassPort) {

            if (callbacks && callbacks.hasOwnProperty('error')) {
                callbacks.error("MRLauncher not running");
            }

            return;
        }


        var url = "http://localhost:" + LocalServerApiClassPort + "/set_account?jsoncollback=?";
        $.ajax({
            url: url,
            data: {
                account: account,
                selected: selected,
                context: context,
               // account_type:account_type
            },
            dataType: 'jsonp',
            cache: false,
            success: function (data) {

                if (callbacks && callbacks.hasOwnProperty('success')) {
                    callbacks.success($.parseJSON(data));
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {

                if (callbacks && callbacks.hasOwnProperty('error')) {
                    callbacks.error(thrownError);
                }
            }
        });
    }


    this.RefreshLocalDatabase = function (context, callbacks) {


        if (!LocalServerApiClassPort) {

            if (callbacks && callbacks.hasOwnProperty('error')) {
                callbacks.error("MRLauncher not running");
            }

            return;
        }


        var url = "http://localhost:" + LocalServerApiClassPort + "/refresh_local_database?jsoncollback=?";
        $.ajax({
            url: url,
            data: {
                context: context
            },
            dataType: 'jsonp',
            cache: false,
            success: function (data) {

                if (callbacks && callbacks.hasOwnProperty('success')) {
                    callbacks.success($.parseJSON(data));
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {

                if (callbacks && callbacks.hasOwnProperty('error')) {
                    callbacks.error(thrownError);
                }
            }
        });
    }

    this.AddLikeServer=function(id, name, top, slogan, version, favicon, site, ip, rating, votes, players, max_players, color, flag,callbacks)
    {
        var url = "http://localhost:" + LocalServerApiClassPort + "/add_like_server?jsoncollback=?";
        $.ajax({
            url: url,
            data: {
                id: id,
                name: name,
                top: top,
                slogan: slogan,
                version: version,
                favicon: favicon,
                site: site,
                ip: ip,
                rating: rating,
                votes: votes,
                players: players,
                max_players: max_players,
                color: color,
                flag: flag
            },
            dataType: 'jsonp',
            cache: false,
            success: function (data) {

                if (callbacks && callbacks.hasOwnProperty('success')) {
                    callbacks.success($.parseJSON(data));
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {

                if (callbacks && callbacks.hasOwnProperty('error')) {
                    callbacks.error(thrownError);
                }
            }
        });
    }

    this.setLink=function(type,id,callbacks){
        console.log(type,id);
        var url = "http://localhost:" + LocalServerApiClassPort + "/set_link?jsoncollback=?";
        $.ajax({
            url: url,
            data: {
                type:type,
                id:id
            },
            dataType: 'jsonp',
            cache: false,
            success: function (data) {

                if (callbacks && callbacks.hasOwnProperty('success')) {
                    callbacks.success($.parseJSON(data));
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {

                if (callbacks && callbacks.hasOwnProperty('error')) {
                    callbacks.error(thrownError);
                }
            }
        });
    }

    this.RefreshLocalDatabaseStatus = function ( callbacks) {

        if (!LocalServerApiClassPort) {

            if (callbacks && callbacks.hasOwnProperty('error')) {
                callbacks.error("MRLauncher not running");
            }

            return;
        }


        var url = "http://localhost:" + LocalServerApiClassPort + "/refresh_local_database_status?jsoncollback=?";
        $.ajax({
            url: url,
            data: {

            },
            dataType: 'jsonp',
            cache: false,
            success: function (data) {

                if (callbacks && callbacks.hasOwnProperty('success')) {
                    callbacks.success($.parseJSON(data));
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {

                if (callbacks && callbacks.hasOwnProperty('error')) {
                    callbacks.error(thrownError);
                }
            }
        });
    }



}


LocalServerApi = new LocalServerApiClass();


/*
 *  $('.btn-start-game').bind('click', function(){
 var data_ip= $(this).attr("ip");
 var data_server_name=$(this).attr("server_name");
 var data_version=$(this).attr("version");

 if(data_ip.indexOf(":")<0){

 data_ip=data_ip+":25565";
 }

 if(data_version.indexOf(",")>=0){

 data_version=data_version.split(",")[0]+"";

 data_version= data_version.substr(0,  data_version.length-2);

 }
 LocalServerApi.StartGame(data_ip,data_server_name,data_version,{

 success:function(data){


 },
 error:function(e){

 alert(e);
 }
 })
 });
 */