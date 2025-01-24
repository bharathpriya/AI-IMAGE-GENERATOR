
import { createRoot } from 'react-dom/client'
import {BrowserRouter} from 'react-router-dom'
import AppContextPorvider from './context/AppContext.jsx'
import './index.css'
import App from './App.jsx'
createRoot(document.getElementById('root')).render(

  <BrowserRouter>
    <AppContextPorvider>
        <App />
        </AppContextPorvider>
 </BrowserRouter>
)
