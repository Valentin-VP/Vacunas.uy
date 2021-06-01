var res = new Vue({
  el: "#res",
  data: {
   
    listaReservas: [],
    reserva:''
  
  },
  mounted() {
    this.getReservas();
  },




    methods: 
    {
      aEliminar() {
        
        reserva=this.reserva;
        console.log('lista Eliminar: ', this.reserva);
        this.eliminarDatos();
        
      },
      
    //  eliminarDatos () {

    
      
    // axios.delete("/grupo15-services/rest/reservas/eliminar?p=idPlan&e=idEtapa&date=yyyy-MM-dd", {

    //  reserva : this.reserva.toString(),
    
     
   //});
  
getReservas () {
        
         
  axios.get("http://localhost:8080/grupo15-services/rest/reservas/listar")
  .then((response => {

    console.log('lista Reservas: ', response.data)
    this.listaReservas = response.data;


  }))


      },

      

            },

        

            

              
  
     
  
   })