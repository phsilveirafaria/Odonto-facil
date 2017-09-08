angular.module('odontoFacil').controller("funcionarioController", ['$mdDialog', 'funcionarioFactory', 'NgTableParams', '$scope', '$location', '$http' ,
	function($mdDialog, funcionarioFactory, NgTableParams, $scope, $location, $http) {
	var ctrl = this;

	ctrl.funcionarios = [];
	ctrl.funcionario = {};
	
	$scope.$watch(function () { return ctrl.funcionarios; }, function (newValue, oldValue) {
		ctrl.tableParams = new NgTableParams({ count: 10, sorting: { nomeCompleto: "asc" } }, { counts: [], dataset: ctrl.funcionarios });
	});
	
	if (funcionarioFactory.isEditandoFuncionario()) {
		ctrl.funcionario = funcionarioFactory.getFuncionario();		
	}

	// primeiro parametro, sucesso, segundo parametro erro.
	ctrl.listarFuncionarios = function() {
		funcionarioFactory.listarFuncionarios().then(function successCallback(response) {
			ctrl.funcionarios = response.data;
		}, function errorCallback(response) {
			console.log(response.data);
			console.log(response.status);
		});

	}
	
	ctrl.excluirFuncionarios = function(funcionario) {
		console.log(cliente);
		var confirm = $mdDialog.confirm()
		.title('Atenção')
		.textContent('Todas as informações do paciente, incluindo os prontuários, serão perdidas. Tem certeza que deseja continuar?')				
		.ok('Sim')
		.cancel('Não');
		
		funcionarioFactory.excluirFuncionarios(funcionario).then(function successCallback(response) {
			ctrl.listarFuncionarios();
			ctrl.funcionarios = response.data;
			console.log(response.data);
			console.log(response.status);
		}, function errorCallback(response) {
			console.log(response.data);
			console.log(response.status);
		});
		
	}
	
	ctrl.buscarCep = function(nCep) {
		ctrl.loading = true;
		
		var cep = nCep.replace(/[^0-9]/g,'');
		$http.get("https://viacep.com.br/ws/" + cep + "/json/").then(
				successCallback = function(response) {
					ctrl.funcionario.rua = response.data.logradouro;
					ctrl.funcionario.numero = "";
					ctrl.funcionario.bairro = response.data.bairro;
					ctrl.funcionario.cidade = response.data.localidade;
					ctrl.funcionario.uf = response.data.uf;
					ctrl.loading = false;					
				},
			  	errorCallback = function (error, status){
					ctrl.funcionario.logradouro = "";
					ctrl.funcionario.numero = "";
					ctrl.funcionario.bairro = "";
					ctrl.funcionario.localidade = "";
					ctrl.funcionario.uf = "";
					ctrl.loading = false;
			  	}		
		);						
	};
	
	ctrl.editarFuncionario = function(funcionario) {
		if (funcionario.dataNascimento) {
			var dataFormatada = new Date(funcionario.dataNascimento);			
			var dia = dataFormatada.getDate() < 10?"0"+dataFormatada.getDate():dataFormatada.getDate();
			var mes = (dataFormatada.getMonth()+1) < 10?"0"+(dataFormatada.getMonth()+1):(dataFormatada.getMonth()+1);
			var ano = dataFormatada.getFullYear();			
			funcionario.dataNascimento = dia + "/" + mes + "/" + ano;
		}
		funcionarioFactory.setFuncionario(funcionario);
		funcionarioFactory.setEditandoFuncionario(true);
		console.log(funcionario);
		$location.path("/editarFuncionario");
	};
	
	
	ctrl.salvarFuncionarios = function(funcionario) {
		funcionarioFactory.salvarFuncionarios(funcionario).then(function successCallback(response) {
			$mdDialog.show(
					$mdDialog.alert()
						.clickOutsideToClose(true)
						.title('Cadastro de Funcionário')
						.textContent('Funcionário cadastrado com sucesso!')
						.ariaLabel('Alerta')
						.ok('Ok')						
				);	
			ctrl.funcionario = {};	
			ctrl.funcionarios = response.data;
			$scope.frmFuncionario.$setPristine();
		}, function errorCallback(response) {
			console.log(response.data);
			console.log(response.status);
		});
		
	}
	
	ctrl.listarFuncionarios();
}]);