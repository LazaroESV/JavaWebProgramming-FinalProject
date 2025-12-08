import { apiGet, apiPost, apiPut, apiDelete } from "./api";

export const CardsAPI = {
    list: () => apiGet("/cards"),
    get: (id) => apiGet(`/cards/${id}`),
    create: (payload) => apiPost("/cards", payload),
    update: (id, payload) => apiPut(`/cards/${id}`, payload),
    remove: (id) => apiDelete(`/cards/${id}`),
    
    search: async (q) => {
        try {
            return await apiGet("/cards/search", q);
        } catch (e) {
            if (e?.status === 404 || e?.status === 405) {
                return apiGet("/cards", q);
            }
        throw e;
        }
    },
}; 