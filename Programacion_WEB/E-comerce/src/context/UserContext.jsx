// src/context/UserContext.jsx
import React, { createContext, useState, useEffect, useCallback } from 'react';

// Crea el contexto de usuario. Se inicializa aquí, pero el valor lo proveerá UserProvider.
export const UserContext = createContext();

export const UserProvider = ({ children }) => {
    // Inicializa userToken y userData usando funciones para useState.
    // Esto asegura que localStorage.getItem se ejecute SOLO una vez en la inicialización
    // para evitar lecturas innecesarias en cada renderizado.
    const [userToken, setUserToken] = useState(() => {
        // Lee el token desde localStorage. La clave ahora es 'token'.
        return localStorage.getItem('token') || null;
    });

    const [userData, setUserData] = useState(() => {
        // Lee los datos del usuario desde localStorage. La clave ahora es 'userData'.
        const storedUserData = localStorage.getItem('userData');
        try {
            // Intenta parsear los datos. Si están corruptos, devuelve null y los limpia.
            return storedUserData ? JSON.parse(storedUserData) : null;
        } catch (e) {
            console.error("Error parsing userData from localStorage:", e);
            localStorage.removeItem('userData'); // Limpiar datos corruptos
            return null;
        }
    });

    // Estado para controlar si los datos del usuario están en proceso de carga/validación.
    const [isLoading, setIsLoading] = useState(true);

    // Usa useCallback para memoizar las funciones de login y logout.
    // Esto previene que estas funciones se re-creen en cada render, lo que puede
    // ayudar a optimizar componentes hijos que las reciben como props.
    const login = useCallback((token, user_data) => {
        // Guarda el token y los datos del usuario en localStorage
        localStorage.setItem('token', token);
        localStorage.setItem('userData', JSON.stringify(user_data));
        // Actualiza los estados locales del contexto
        setUserToken(token);
        setUserData(user_data);
        console.log("Login user_data:", user_data); // DEBUG: Para verificar los datos de usuario al logear
        console.log("Login is_admin:", user_data?.is_admin); // DEBUG: Para verificar si is_admin es true/false
    }, []); // Dependencias vacías: la función se crea una sola vez.

    const logout = useCallback(() => {
        // Elimina el token y los datos del usuario de localStorage
        localStorage.removeItem('token');
        localStorage.removeItem('userData');
        // Limpia los estados locales del contexto
        setUserToken(null);
        setUserData(null);
        console.log("Usuario deslogeado."); // DEBUG
    }, []); // Dependencias vacías: la función se crea una sola vez.

    // Este useEffect es crucial para la persistencia de la sesión y la revalidación.
    // Se ejecuta en el montaje inicial y cada vez que userToken, userData o logout cambien.
    useEffect(() => {
        const fetchUserData = async () => {
            // Si hay un token pero los datos del usuario están incompletos o faltan,
            // intenta recuperarlos del backend.
            if (userToken && (!userData || !userData.id)) {
                try {
                    setIsLoading(true); // Establece el estado de carga a true
                    const response = await fetch('http://localhost:5000/api/account/profile', {
                        headers: {
                            'Authorization': `Bearer ${userToken}` // Envía el token para autenticar
                        }
                    });

                    if (response.ok) {
                        const data = await response.json();
                        setUserData(data); // Actualiza los datos del usuario en el estado
                        localStorage.setItem('userData', JSON.stringify(data)); // Y en localStorage
                        console.log("Fetched user data from /profile:", data); // DEBUG
                        console.log("Fetched is_admin from /profile:", data?.is_admin); // DEBUG
                    } else {
                        // Si el token no es válido (ej. expiró, manipulado), cierra la sesión.
                        console.error('Error al obtener datos del usuario desde /profile:', response.status, response.statusText);
                        logout();
                    }
                } catch (error) {
                    // Manejo de errores de red o del servidor
                    console.error('Error de red al obtener datos del usuario desde /profile:', error);
                    logout(); // En caso de error, también cierra la sesión
                } finally {
                    setIsLoading(false); // La carga ha terminado (éxito o fallo)
                }
            } else if (!userToken) {
                // Si no hay token, asegura que no haya datos de usuario y la carga ha terminado.
                setUserData(null);
                localStorage.removeItem('userData');
                setIsLoading(false);
            } else {
                // Si ya hay token y userData cargado, simplemente deja de cargar.
                setIsLoading(false);
            }
        };

        fetchUserData(); // Llama a la función asíncrona
    }, [userToken, userData, logout]); // Dependencias: el efecto se ejecuta si estas cambian.

    // No renderices los componentes hijos hasta que el estado inicial de carga se haya resuelto.
    // Esto previene que se muestre contenido para un usuario no autenticado por un instante.
    if (isLoading) {
        return <div>Cargando datos de usuario...</div>; // Puedes usar un spinner o un componente de carga aquí.
    }

    // Provee los valores del contexto a los componentes hijos.
    return (
        <UserContext.Provider value={{ userToken, userData, isLoading, login, logout }}>
            {children}
        </UserContext.Provider>
    );
};