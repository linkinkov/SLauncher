$(function(){

$(document).bind('selectstart dragstart', function(e) {
        e.preventDefault();
        return false;
    });

$(document).bind('contextmenu',function(e){
return false;

})

    $('#site').bind('click', function(){

    var val=  $('#site').attr("name")
   var type="exitFrameSite"
       // app.exitFrameSite(val);
        LocalServerApi.setLink(type,val,{
            success: function (data) {
                console.log(data);
            },
            error: function (e) {
                alert(e);
            }
        })
    })


  $('.btn-close-this').bind('click', function(){


      var type="closeThisDialogButton"
      var val=''
      LocalServerApi.setLink(type,val,{
          success: function (data) {
              console.log(data);
          },
          error: function (e) {
              alert(e);
          }
      })
       // app.closeThisDialogButton();
    })

    $('.btn-exit').bind('click', function(){
console.log("exitButton")
        var type="exitButton"
        var val=''
        LocalServerApi.setLink(type,val,{
            success: function (data) {
                console.log(data);
            },
            error: function (e) {
                alert(e);
            }
        })


      //  app.exitButton();
    })





})