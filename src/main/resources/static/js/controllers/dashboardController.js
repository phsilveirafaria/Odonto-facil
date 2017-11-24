angular.module('odontoFacil').controller('dashboardController', ['$scope', '$rootScope', '$http', '$location', 
	'$mdDialog', 'funcionarioFactory' , 'Session', 'homeFactory', 'agendamentoFactory', 'financeiroFactory', 'utilService', 'dashboardFactory',	function($scope, $rootScope, 
			$http, $location, $mdDialog, funcionarioFactory, Session, homeFactory, agendamentoFactory, financeiroFactory, utilService, dashboardFactory) {
	
	var ctrl = this;
	ctrl.funcionario = {};
	ctrl.valores = {};
	ctrl.consultasDoMesFuncionario = {};
	ctrl.x = Session.usuario;
	ctrl.lstAgendamentos = {};

	
	  var carregarContasDoMes = function() {		
		  var dataInicial = moment().startOf('month').local().format();
		  var dataFinal = moment().endOf('month').local().format();
		
		  financeiroFactory.listarDespesasPorPeriodo(dataInicial, dataFinal).then(
				successCallback = function(response) {
					ctrl.totalDespesasMesCorrente = response.data.totalDespesas;
				},
				errorCallback = function(error) {
					utilService.tratarExcecao(error);
				}
		  );
		
		  financeiroFactory.listarConsultasPorPeriodo(dataInicial, dataFinal).then(
				successCallback = function(response) {
					ctrl.totalConsultasMesCorrente = response.data.totalConsultas;
				},
				errorCallback = function(error) {					
					utilService.tratarExcecao(error);
				}
		  );
	  };
	  
	  
	  var listarValores = function() {
		  dashboardFactory.listarValores().then(
			      successCallback = function(response) {		    	  
			    	  ctrl.valores = response.data;	    	  
			  	  },
			  	  errorCallback = function (error, status){			  		
			  	  }
			  );
	  }
	  
	  var listarAgendamentosDoMesPorFuncionario = function() {
		  dashboardFactory.listarValores().then(
			      successCallback = function(response) {		    	  
			    	  ctrl.consultasDoMesFuncionario = response.data;	    	  
			  	  },
			  	  errorCallback = function (error, status){			  		
			  	  }
			  );
	  }
	  
	  var carregarAniversariantesDoMes = function() {
		  funcionarioFactory.listarAniversariantesDoMes().then(
			      successCallback = function(response) {		    	  
			    	  ctrl.lstAniversariantes = response.data;	    	  
			  	  },
			  	  errorCallback = function (error, status){			  		
			  	  }
			  );
	  }
	  
	  var carregarAgendamentosDoDia = function() {
		  agendamentoFactory.listarAgendamentosDoDia().then(
			      successCallback = function(response) {		    	  
			    	  ctrl.lstAgendamentos = response.data;					    	  			    	 
			  	  },
			  	  errorCallback = function (error, status){		
			  		  utilService.tratarExcecao(error);
			  	  }
			  );
	  }
	  
	  var carregarAgendamentosDoMes = function() {
		  agendamentoFactory.listarAgendamentosDoMes().then(
			      successCallback = function(response) {		    	  
			    	  ctrl.lstAgendamentosDoMes = response.data;					    	  			    	 
			  	  },
			  	  errorCallback = function (error, status){		
			  		  utilService.tratarExcecao(error);
			  	  }
			  );
	  }
	
	  ctrl.funcionarioLogado = function(x) {
			/*homeFactory.funcionarioLogado(nomeFuncionario).then(function successCallback(response){
			Session.create(response.data);
			ctrl.funcionario = response.data;
			return ctrl.funcionario;
		}, function errorCallback(response) {
			console.log(response.data);
			console.log(response.status);
		});*/
			$http({
				  method: 'GET',
				  url: 'https://localhost:8443/userLogado/'
				}).then(function successCallback(response) {
				    Session.create(response.data);
				    ctrl.x = response.data;
				  }, function errorCallback(response) {
				    // called asynchronously if an error occurs
				    // or server returns response with an error status.
				  });
			
		}
	listarAgendamentosDoMesPorFuncionario();
	listarValores();
	carregarContasDoMes();
	carregarAniversariantesDoMes();
	carregarAgendamentosDoDia();
	ctrl.funcionarioLogado();
}]);