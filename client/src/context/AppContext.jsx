import { createContext, useEffect, useState } from "react";
import axios from "axios";

export const AppContext = createContext();

const AppContextProvider = (props) => {

    const [user, setUser] = useState(null);
    const [showLogin, setShowLogin] = useState(false);
    const [credits, setCredits] = useState(0);

    const backendUrl = import.meta.env.VITE_BACKEND_URL || "http://localhost:8082";

    const loadCreditsData = async () => {
        if (user) {
            try {
                const { data } = await axios.get(`${backendUrl}/api/user/credits?userId=${user.id}`);
                if (data.success) {
                    setCredits(data.credits);
                }
            } catch (error) {
                console.error(error);
            }
        }
    };

    const logout = () => {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        setUser(null);
    };

    useEffect(() => {
        const storedUser = localStorage.getItem('user');
        if (storedUser) {
            setUser(JSON.parse(storedUser));
        }
    }, []);

    useEffect(() => {
        if (user) {
            loadCreditsData();
        }
    }, [user]);

    const value = {
        user, setUser, showLogin, setShowLogin, backendUrl, credits, setCredits, loadCreditsData, logout
    };

    return (
        <AppContext.Provider value={value}>
            {props.children}
        </AppContext.Provider>
    );
};

export default AppContextProvider;