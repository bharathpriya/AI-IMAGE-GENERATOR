import { createContext, useState } from "react";

export const AppContext = createContext();

const AppContextPorvider = (props)=>{

    const [user, setUser ] = useState(null);
   const [showLogin, setShowLogin] = useState(false);

    const value ={
        user, setUser, showLogin, setShowLogin
    }

    return (
        <AppContext.Provider value={value}>
                {props.children}
        </AppContext.Provider>
    )
    
}
export default AppContextPorvider