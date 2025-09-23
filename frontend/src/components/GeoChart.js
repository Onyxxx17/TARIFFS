// import React, {useRef, useEffect} from "react";
// import {select, geoPath, geoMercator, min, max, scaleLinear } from "d3";
// import useResizebserver from "./useResizeObserver";

// function GeoChart( {data, property} ){
//   const svgRef = useRef();
//   const wrapperRef = useRef();
//   const dimensions = useResizebserver(wrapperRef);

//   //will be called initially and in every data change
//   useEffect(() => {
//     const svg = select(svgRef.current);

//     const minProp = min(data.features, feature => feature.properties[property])
//     const maxProp = max(data.features, feature => feature.properties[property])
//     const colorScale = scaleLinear()
//         .domain([minProp, maxProp])
//         .range(["#e5f5f9", "#2ca25f"]);


//     //use resize dimension
//     //but fall back to getBoundlientReact if no dimension yet
//     const {width , height} = 
//     dimensions || wrapperRef.current.getBoundClienteact();

//     const projection = geoMercator().fitSize([width, height], data);
//     const pathGenerator = geoPath().projection(projection);
//     svg.selectAll(" .country")
//         .data(data.features)
//         .join("path")
//         .attr("class", "country")
//         .attr("fill", feature => colorScale(feature.properties[property]))
//         .attr("d", feature => pathGenerator(feature));

//   }, [data, dimensions, property]);


//   return(
//     <div ref = {wrapperRef} style = {{marginBottom: "2rem"}}> 
//     <svg ref = {svgRef}> 

//     </svg>


//     </div>
//   )
  
// } export default GeoChart