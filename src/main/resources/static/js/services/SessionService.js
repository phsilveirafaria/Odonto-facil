angular.module('odontoFacil').service('Session', function() {
	
	this.create = function(usuario) {
		this.usuario = usuario;
	};
	this.destroy = function() {
		this.usuario = null;
	};
})