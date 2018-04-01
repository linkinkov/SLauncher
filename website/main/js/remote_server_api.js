LocalServerApiClassPort = 8000;
function RemoteServerApiClass(){

    this.getVersions=function(callbacks){

        $.ajax({
            //url: "http://minecraftrating.ru/mrlauncher_api/get_versions.php?callback=?",
            url:"http://localhost:" + LocalServerApiClassPort + "/getServerVersions?jsoncollback=?",
            dataType: 'jsonp',
            //jsonpCallback: 'ver_callback',
            cache: true,
            success: function(data) {

                if(callbacks && callbacks.hasOwnProperty('success')){
                    callbacks.success($.parseJSON(data));
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {

                if(callbacks && callbacks.hasOwnProperty('error')){
                    callbacks.error(thrownError);
                }
            }
        });
    }


    this.getParams=function(type, callbacks){

        $.ajax({
          //  url: "http://minecraftrating.ru/mrlauncher_api/get_params.php?type="+type+"&callback=?",
            url:"http://localhost:" + LocalServerApiClassPort + "/getServerParams?jsoncollback=?",
            data:{
                type:type
            },
            dataType: 'jsonp',
            //jsonpCallback: type+'_callback',
            cache: true,
            success: function(data) {

                if(callbacks && callbacks.hasOwnProperty('success')){
                    callbacks.success($.parseJSON(data));
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {
                if(callbacks && callbacks.hasOwnProperty('error')){
                    callbacks.error(thrownError);
                }
            }
        });
    }


    this.getServers=function(version, param,page,count_on_page,query, callbacks){

       /* var url="http://minecraftrating.ru/mrlauncher_api/get_servers.php?param_url="+param+"&version="+version+"&callback=?";

        if(version=="all" && param=="all"){

            url="http://minecraftrating.ru/mrlauncher_api/get_servers.php?callback=?";

        }else  if(version=="all" ){

            url="http://minecraftrating.ru/mrlauncher_api/get_servers.php?param_url="+param+"&callback=?";

        }else if(param=="all" ){

            url="http://minecraftrating.ru/mrlauncher_api/get_servers.php?version="+version+"&callback=?";
        }*/
        if(version=="all"){
            version="";
        }
        if(param=="all"){
            param="";
        }
       var url="http://localhost:" + LocalServerApiClassPort + "/getServers?jsoncollback=?";

        $.ajax({
            url: url,
            data:{
                version:version,
                param:param,
                page:page,
                count_on_page:count_on_page,
                query:(query)
            },
            dataType: 'jsonp',
           // jsonpCallback: 'servers',
            cache: false,
           
            success: function(data) {

                if(callbacks && callbacks.hasOwnProperty('success')){


                    var sort_by = function(field, reverse, primer){

                        var key = primer ?
                            function(x) {return primer(x[field])} :
                            function(x) {return x[field]};

                        reverse = !reverse ? 1 : -1;

                        return function (a, b) {
                            return a = key(a), b = key(b), reverse * ((a > b) - (b > a));
                        }
                    }


                    var servers_data=$.parseJSON(data);


                   /* var servers =[];


                    for (var server_id in servers_data.servers){

                        servers.push(servers_data[server_id]);
                    }

                    servers.sort(sort_by('top', false, parseInt));*/

                    callbacks.success(servers_data);
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {
                if(callbacks && callbacks.hasOwnProperty('error')){
                    callbacks.error(thrownError);
                }
            }
        });
    }
   /* this.setStatus=function (status,hash,callbacks) {
       //  var url="http://minecraftrating.ru/mrlauncher_api/get_servers.php?param_url="+param+"&version="+version+"&callback=?";
        var url="http://minecraftrating.ru/mrlauncher_api/stats.php?type="+status+"&user_hash="+hash
     
        $.ajax({
            url: url,
            dataType: 'jsonp',
            cache: false,
            success: function(data) {


                if(callbacks && callbacks.hasOwnProperty('success')){
                    callbacks.success($.parseJSON(data));
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {
                if(callbacks && callbacks.hasOwnProperty('error')){
                    callbacks.error(thrownError);
                }
            }
        });

    }*/
    this.getServersSearch=function(version, param,page,count_on_page,query, callbacks){

        /* var url="http://minecraftrating.ru/mrlauncher_api/get_servers.php?param_url="+param+"&version="+version+"&callback=?";

         if(version=="all" && param=="all"){

         url="http://minecraftrating.ru/mrlauncher_api/get_servers.php?callback=?";

         }else  if(version=="all" ){

         url="http://minecraftrating.ru/mrlauncher_api/get_servers.php?param_url="+param+"&callback=?";

         }else if(param=="all" ){

         url="http://minecraftrating.ru/mrlauncher_api/get_servers.php?version="+version+"&callback=?";
         }*/
        if(version=="all"){
            version="";
        }
        if(param=="all"){
            param="";
        }
        var url="http://localhost:" + LocalServerApiClassPort + "/getServersQuery?jsoncollback=?";

        $.ajax({
            url: url,
            data:{
                version:version,
                param:param,
                page:page,
                count_on_page:count_on_page,
                query: encodeURIComponent(query)
            },
            dataType: 'jsonp',
            // jsonpCallback: 'servers',
            cache: false,

            success: function(data) {

                if(callbacks && callbacks.hasOwnProperty('success')){


                    var sort_by = function(field, reverse, primer){

                        var key = primer ?
                            function(x) {return primer(x[field])} :
                            function(x) {return x[field]};

                        reverse = !reverse ? 1 : -1;

                        return function (a, b) {
                            return a = key(a), b = key(b), reverse * ((a > b) - (b > a));
                        }
                    }


                    var servers_data=$.parseJSON(data);


                    /* var servers =[];


                     for (var server_id in servers_data.servers){

                     servers.push(servers_data[server_id]);
                     }

                     servers.sort(sort_by('top', false, parseInt));*/

                    callbacks.success(servers_data);
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {
                if(callbacks && callbacks.hasOwnProperty('error')){
                    callbacks.error(thrownError);
                }
            }
        });
    }

}

RemoteServerApi=new RemoteServerApiClass();