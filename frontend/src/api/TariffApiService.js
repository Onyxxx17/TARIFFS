import { apiClient } from './ApiClient'

export const retrieveAllTodosForUsernameApiByToCountryCode
    = (countryCode) => apiClient.get(`/api/tariff-rules/to-country/${countryCode}`)

export const retriveTariffRulesByCriteria 
    = (fromCountryName, toCountryName, effectiveYear, productName, productId) => apiClient.get(`/api/tariff-rules/search?fromCountryName=${fromCountryName}&toCountryName=${toCountryName}&effectiveYear=${effectiveYear}&productName=${productName}&productId=${productId}`)
