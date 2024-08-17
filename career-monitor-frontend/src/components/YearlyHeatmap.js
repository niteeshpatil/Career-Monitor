import React, { useEffect, useRef } from "react";
import * as d3 from "d3";
import "./Heatmap.css";

const Heatmap = ({ data, year }) => {
  const svgRef = useRef();

  useEffect(() => {
    const width = 900;
    const height = 150;
    const cellSize = 17;
    const padding = 20;
    const colors = ["#ebedf0", "#c6e48b", "#7bc96f", "#239a3b", "#196127"];
    const marginLeft = 0; // Margin for the left
    const marginRight = 20; // Margin for the right

    // Clear previous SVG content
    d3.select(svgRef.current).selectAll("*").remove();

    const svg = d3
      .select(svgRef.current)
      .attr("width", width + marginLeft + marginRight)
      .attr("height", height)
      .style("margin", "20px")
      .append("g")
      .attr("transform", `translate(${padding + marginLeft},${padding})`);

    const parseDate = d3.timeParse("%Y-%m-%d");
    const formatDate = d3.timeFormat("%Y-%m-%d");

    const dataByDate = data.reduce((acc, { date, count }) => {
      const parsedDate = parseDate(date);
      if (parsedDate) {
        acc[formatDate(parsedDate)] = count; // Store as formatted date string
      }
      return acc;
    }, {});

    const startOfWeek = (d) => d.getDay();
    const formatMonth = d3.timeFormat("%b");

    const maxCount = d3.max(data, (d) => d.count);

    const colorScale = d3.scaleQuantize().domain([0, maxCount]).range(colors);

    let startDate, endDate;
    if (year === new Date().getFullYear()) {
      // Display data from today to one year ago
      endDate = new Date();
      startDate = new Date(endDate);
      startDate.setFullYear(endDate.getFullYear() - 1);
    } else {
      // Display data for the selected year (January 1st to December 31st)
      startDate = new Date(year, 0, 1);
      endDate = new Date(year, 11, 31);
    }

    const days = d3.timeDays(startDate, d3.timeDay.offset(endDate, 1));

    const months = svg
      .append("g")
      .selectAll("g")
      .data(d3.timeMonths(startDate, endDate))
      .join("g");

    months
      .append("text")
      .attr("x", (d) => d3.timeWeek.count(startDate, d) * cellSize)
      .attr("y", -5)
      .text(formatMonth);

    const dayRects = svg
      .append("g")
      .selectAll("rect")
      .data(days)
      .join("rect")
      .attr("width", cellSize - 2)
      .attr("height", cellSize - 2)
      .attr("x", (d) => d3.timeWeek.count(startDate, d) * cellSize)
      .attr("y", (d) => startOfWeek(d) * cellSize)
      .attr("fill", (d) => colorScale(dataByDate[formatDate(d)] || 0)); // Use formatted date string for lookup

    // Tooltip
    const tooltip = d3
      .select("body")
      .append("div")
      .attr("class", "tooltip")
      .style("opacity", 0)
      .style("position", "absolute")
      .style("padding", "5px")
      .style("background-color", "white")
      .style("border", "1px solid #ccc")
      .style("border-radius", "3px")
      .style("pointer-events", "none");

    dayRects
      .on("mouseover", function (event, d) {
        const count = dataByDate[formatDate(d)] || 0;
        tooltip.transition().duration(200).style("opacity", 0.9);
        tooltip
          .html(
            `<strong>Date:</strong> ${formatDate(
              d
            )}<br/><strong>Count:</strong> ${count}`
          )
          .style("left", `${event.pageX + 5}px`)
          .style("top", `${event.pageY - 28}px`);
      })
      .on("mouseout", function () {
        tooltip.transition().duration(500).style("opacity", 0);
      });
  }, [data, year]);

  return <svg ref={svgRef}></svg>;
};

export default Heatmap;
