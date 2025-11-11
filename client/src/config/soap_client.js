import { createClientAsync } from "soap";

class SoapClient {
  constructor() {
    this.wsdlUrl = "http://localhost:8080/ws/currencyService/currency.wsdl";
    this.serviceUrl = "http://localhost:8080/ws";
    this.client = null;
  }

  // inicializa cliente
  async initialize() {
    if (this.client) return this.client;

    try {
      this.client = await createClientAsync(this.wsdlUrl, {
        endpoint: this.serviceUrl,
        forceSoap12Headers: false,
      });

      return this.client;
    } catch (error) {
      console.error(error);
      console.error("Erro ao inicializar cliente SOAP:", error.message);
      throw error;
    }
  }

  async callMethod(methodName, args) {
    try {
      if (!this.client) await this.initialize();

      const method = this.client[`${methodName}Async`];

      if (typeof method !== "function") {
        throw new Error(`metodo ${methodName} nao encontrado no cliente!`);
      }

      const [result, response] = await method(args);
      return [result, response];
    } catch (error) {
      console.error( `Erro na chamada SOAP: `, error && error.message ? error.message : error);
      throw error;
    }
  }
}

export default new SoapClient();
