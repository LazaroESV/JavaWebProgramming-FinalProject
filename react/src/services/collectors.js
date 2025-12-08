import { apiGet, apiPost, apiPut, apiDelete } from "./api";

export const CollectorsAPI = {
    list: () => apiGet("/collectors"),
    get: (id) => apiGet(`/collectors/${id}`),
    create: (payload) => apiPost("/collectors", payload),
    update: (id, payload) => apiPut(`/collectors/${id}`, payload),
    remove: (id) => apiDelete(`/collectors/${id}`),
    search: (q) => apiGet("/collectors/search", q),
    carsByOwner: (id) => apiGet(`/collectors/${id}/cards`)
}; 
