angular.module('odontoFacil').controller("clienteController", ['clienteFactory','$mdDialog', 'NgTableParams', '$scope' , '$location', 
	function(clienteFactory, $mdDialog, NgTableParams, $scope, $location) {
	var ctrl = this;

	/*
	$scope.clientes = [];
	$scope.cliente={};
	*/
	ctrl.clientes = [];
	ctrl.cliente = {};
	
	$scope.$watch(function () { return ctrl.clientes; }, function (newValue, oldValue) {
		ctrl.tableParams = new NgTableParams({ count: 10, sorting: { nomeCompleto: "asc" } }, { counts: [], dataset: ctrl.clientes });
	});
	
	if (clienteFactory.isEditandoCliente()) {
		ctrl.cliente = clienteFactory.getCliente();		
	}

	ctrl.editarCliente = function(cliente) {
		if (cliente.dataNascimento) {
			var dataFormatada = new Date(cliente.dataNascimento);			
			var dia = dataFormatada.getDate() < 10?"0"+dataFormatada.getDate():dataFormatada.getDate();
			var mes = (dataFormatada.getMonth()+1) < 10?"0"+(dataFormatada.getMonth()+1):(dataFormatada.getMonth()+1);
			var ano = dataFormatada.getFullYear();			
			cliente.dataNascimento = dia + "/" + mes + "/" + ano;
		}
		clienteFactory.setCliente(cliente);
		clienteFactory.setEditandoCliente(true);
		console.log(cliente);
		$location.path("/editarCliente");
	};
	
	// primeiro parametro, sucesso, segundo parametro erro.
	ctrl.listarClientes = function() {
		clienteFactory.listarClientes().then(function successCallback(response) {
			ctrl.clientes = response.data;
			console.log(response.data);
			console.log(response.status);
		}, function errorCallback(response) {
			console.log(response.data);
			console.log(response.status);
		});

	}
	
	ctrl.excluirClientes = function(cliente) {
		console.log(cliente);
		var confirm = $mdDialog.confirm()
		.title('Atenção')
		.textContent('Todas as informações do paciente, incluindo os prontuários, serão perdidas. Tem certeza que deseja continuar?')				
		.ok('Sim')
		.cancel('Não');
		
		clienteFactory.excluirClientes(cliente).then(function successCallback(response) {
			ctrl.listarClientes();
			ctrl.clientes = response.data;
			console.log(response.data);
			console.log(response.status);
		}, function errorCallback(response) {
			console.log(response.data);
			console.log(response.status);
		});
		
	}
	
	
	ctrl.salvarClientes = function(cliente) {
		clienteFactory.salvarClientes(cliente).then(function successCallback(response) {
			$mdDialog.show(
					$mdDialog.alert()
						.clickOutsideToClose(true)
						.title('Cadastro de Paciente')
						.textContent('Paciente cadastrado com sucesso!')
						.ariaLabel('Alerta')
						.ok('Ok')						
				);	
			ctrl.listarClientes();	
			$scope.frmCliente.$setPristine();
			
			clienteFactory.listarClientes().then(
					successCallback = function(response) {					    
						ctrl.clientes = response.data;	    	  
				  	  },
				  	  errorCallback = function (error, status){
				  		//utilService.tratarExcecao(error); 
				  	  }
			);
		}, function errorCallback(response) {
			console.log(response.data);
			console.log(response.status);
		});
		
	}
	
	ctrl.listarClientes();
}]);