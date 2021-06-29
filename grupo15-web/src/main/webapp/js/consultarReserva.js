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
       
    
  getReservas () {
          
           
    axios.get("/grupo15-services/rest/reservas/listar")
    .then((response => {
  
      console.log('lista Reservas: ', response.data)
      this.listaReservas = response.data;
  
  
    }))
  
  
        },
  
        
  
              },
  
          
  
              
  
                
    
       
    
     })