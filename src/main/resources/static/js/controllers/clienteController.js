angular.module('odontoFacil').controller("clienteController", ['clienteFactory','$mdDialog', 'NgTableParams', '$scope' ,
	function(clienteFactory, $mdDialog, NgTableParams, $scope) {
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
		clienteFactory.excluirClientes(cliente).then(function successCallback(response) {
			ctrl.clientes = response.data;
			console.log(response.data);
			console.log(response.status);
		}, function errorCallback(response) {
			console.log(response.data);
			console.log(response.status);
		});
		
	}
	
	
	ctrl.salvarClientes = function(cliente) {
		console.log(cliente);
		clienteFactory.salvarClientes(cliente).then(function successCallback(response) {
			$mdDialog.show(
					$mdDialog.alert()
						.clickOutsideToClose(true)
						.title('Cadastro de Paciente')
						.textContent('Paciente cadastrado com sucesso!')
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