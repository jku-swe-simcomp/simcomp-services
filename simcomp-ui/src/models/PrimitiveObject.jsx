import { useFrame, useLoader } from "@react-three/fiber";
import { useState } from "react";
import { GLTFLoader } from "three/examples/jsm/loaders/GLTFLoader";
const model = require('./niryo_one.glb');

export default function PrimitiveObject() {
    const gltf = useLoader(GLTFLoader, model);
    const [axis1, setAxis1] = useState(gltf.scene.children[0].children[0].children[0].children[0].children[11]);

    function rotateAxis(axis, degree) {
        console.log(axis1);
        axis1.quaternion._w = 20;
        axis1.quaternion._x = 20;
        axis1.quaternion._y = 20;
        axis1.quaternion._z = 20;
        axis1.rotation._x = 20;
        
    };

    useFrame((state, delta, xrFrame) => {
        rotateAxis(1, 30)
    });

    return (
        <>
            <primitive object={gltf.scene} scale={0.4} />
        </>
    );
};