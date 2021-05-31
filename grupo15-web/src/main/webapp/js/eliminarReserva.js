var res = new Vue({
  el: "#res",
  data: {
    fecha:'',
    listaReservas: [],
    aEliminar:''
  },
  mounted() {
    this.getReservas();
  },




    methods: 
    {
      
      
     // EliminarDatos () {

    
      
    //  axios.delete("/grupo15-services/rest/reservas/eliminar?p=idPlan&e=idEtapa&date=yyyy-MM-dd", {

   //   idPlan : this.IdPlan.toString(),
    //  idEtapa : this.IdEtapa.toString(),
    //  fecha : this.fecha.toString(),
     
//    });
  
getReservas () {
        
         
  axios.get("http://localhost:8080/grupo15-services/rest/reservas/listar")
  .then((response => {

    console.log('lista Reservas: ', response.data)
    this.listaReservas = response.data;


  }))


      },

      

            },

        

            

              
  
     
  
   })