package com.ao.fiinikacomercial.model.facturacao;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Subscricao {
	
	

		
		@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private long idSubscricao;
		private String data_hora;
		private long idEmpresafk;
		
	    private long id_pacotefk;
	    private String referencia;
	    private String data_pagamento;
	    private String hora_pagamento;
	    private String data_exp_referencia;
	    private String data_expiracao_pagamento;
	    private String preco;
	    private String estado;
	    private int tipo;
	    private String token;
	    
	    

		public String getToken() {
			return token;
		}
		public void setToken(String token) {
			this.token = token;
		}
		public long getIdSubscricao() {
			return idSubscricao;
		}
		public void setIdSubscricao(long idSubscricao) {
			this.idSubscricao = idSubscricao;
		}
		public long getIdEmpresafk() {
			return idEmpresafk;
		}
		public void setIdEmpresafk(long idEmpresafk) {
			this.idEmpresafk = idEmpresafk;
		}
		public String getData_hora() {
			return data_hora;
		}
		public void setData_hora(String data_hora) {
			this.data_hora = data_hora;
		}
		public long getId_pacotefk() {
			return id_pacotefk;
		}
		public void setId_pacotefk(long id_pacotefk) {
			this.id_pacotefk = id_pacotefk;
		}
		public String getReferencia() {
			return referencia;
		}
		public void setReferencia(String referencia) {
			this.referencia = referencia;
		}
		public String getData_pagamento() {
			return data_pagamento;
		}
		public void setData_pagamento(String data_pagamento) {
			this.data_pagamento = data_pagamento;
		}
		public String getData_exp_referencia() {
			return data_exp_referencia;
		}
		public void setData_exp_referencia(String data_exp_referencia) {
			this.data_exp_referencia = data_exp_referencia;
		}
		public String getData_expiracao_pagamento() {
			return data_expiracao_pagamento;
		}
		public void setData_expiracao_pagamento(String data_expiracao_pagamento) {
			this.data_expiracao_pagamento = data_expiracao_pagamento;
		}
		public String getPreco() {
			return preco;
		}
		public void setPreco(String preco) {
			this.preco = preco;
		}
		public String getEstado() {
			return estado;
		}
		public void setEstado(String estado) {
			this.estado = estado;
		}
		public String getHora_pagamento() {
			return hora_pagamento;
		}
		public void setHora_pagamento(String hora_pagamento) {
			this.hora_pagamento = hora_pagamento;
		}
		public int getTipo() {
			return tipo;
		}
		public void setTipo(int tipo) {
			this.tipo = tipo;
		}
	    
	    
	    
	    

}
