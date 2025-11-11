// src/services/currency-service.js
import soap_client from "../config/soap_client.js";
import soapClient from "../config/soap_client.js";

class CurrencyService {
  constructor() {
    this.soapClient = soapClient;
    soap_client.initialize();
  }

  async getCurrencyByPrefix(prefix) {
    try {
      const [result, raw] = await this.soapClient.callMethod("getCurrencyByPrefix", {prefix});
      return [result, raw];
    } catch (error) {
      throw new Error(`Falha ao buscar currency com prefixo ${prefix}: ${error.message}`);
    }
  }
}

export default new CurrencyService();
