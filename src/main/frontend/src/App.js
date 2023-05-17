import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Airplanes from './pages/Airplanes';
import Flights from './pages/Flights';
import Flight from './pages/Flight';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Airplanes/>} />
        <Route path="/flights/" element={<Flights/>} />
        <Route path="/flight/" element={<Flight/>} />
      </Routes>
    </Router>
  );
}

export default App;
