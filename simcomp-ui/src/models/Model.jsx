import { Canvas } from "@react-three/fiber";
import { Environment, OrbitControls } from "@react-three/drei";
import { Suspense } from "react";
import PrimitiveObject from "./PrimitiveObject";

export default function Model() {
  return (
    <div className="vh-100">
      <Canvas
        size={[`2000px`, `3000px`]}
      >
        <Suspense fallback={null}>
          <PrimitiveObject/>
          <OrbitControls />
          <Environment preset="apartment" background />
        </Suspense>
      </Canvas>
    </div>
  );
}