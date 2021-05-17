var vac = new Vue({
  el: "#vac",
  data: () => ({

  }),

  mounted(){
      Vue.http.interceptors.push({

          request: function (request){
            request.headers['Authorization'] = auth.getAuthHeader()
            return request
          },

          response: function (response) {
            //console.log('status: ' + response.data)
            return response;
          }

        });
  }


 })