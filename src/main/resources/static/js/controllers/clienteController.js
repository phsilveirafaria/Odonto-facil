angular.module('odontoFacil').controller("clienteController", ['clienteFactory', '$http', '$mdDialog', 'NgTableParams', '$scope' , '$location', 
	function(clienteFactory, $http, $mdDialog, NgTableParams, $scope, $location) {
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
	
	ctrl.buscarCep = function(nCep) {
		ctrl.loading = true;
		
		var cep = nCep.replace(/[^0-9]/g,'');
		$http.get("https://viacep.com.br/ws/" + cep + "/json/").then(
				successCallback = function(response) {
					ctrl.cliente.rua = response.data.logradouro;
					ctrl.cliente.numero = "";
					ctrl.cliente.bairro = response.data.bairro;
					ctrl.cliente.cidade = response.data.localidade;
					ctrl.cliente.uf = response.data.uf;
					ctrl.loading = false;					
				},
			  	errorCallback = function (error, status){
					ctrl.cliente.logradouro = "";
					ctrl.cliente.numero = "";
					ctrl.cliente.bairro = "";
					ctrl.cliente.localidade = "";
					ctrl.cliente.uf = "";
					ctrl.loading = false;
			  	}		
		);						
	};

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
						.title('Cadastro de Clientes')
						.textContent('Cliente cadastrado com sucesso!')
						.ariaLabel('Alerta')
						.ok('Ok')						
				);	
			ctrl.cliente = {};	
			ctrl.clientes = response.data;
			$scope.frmCliente.$setPristine();
		}, function errorCallback(response) {
			console.log(response.data);
			console.log(response.status);
		});
		
	}
	
	ctrl.listarClientes();
}]);