
var vac = new Vue({
    el: "#vac",
    data: {
    
      listadoVigentes: [],
      listadoProximas: [],
      
      
    },
    
    mounted() {
       this.getVigentes();
       this.getProximas();
    },
    methods: {
        
    getVigentes () {
        console.log("entre al vigentes")
        axios.get("http://localhost:8080/grupo15-services/rest/planagenda/vigente")
            .then((response => {
            
                console.log(`Get Vigente: `, response.data)
                this.listadoVigentes = response.data
                
          }))
        },

    getProximas () {
        console.log("entre al proximas")
        axios.get("http://localhost:8080/grupo15-services/rest/planagenda/proxima"  )
        .then((response => {
                
        console.log(`Get Proximas: `, response.data)
        this.listadoProximas = response.data
                    
              }))
            },

    },
})

